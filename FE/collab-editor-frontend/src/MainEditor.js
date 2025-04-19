import React, { useEffect, useRef, useState } from 'react';
import Editor from '@monaco-editor/react';
import stompClient from './services/websocket';
import { useNavigate } from 'react-router-dom';
import Sidebar from './Sidebar';

function MainEditor() {
  const [user, setUser] = useState(null);
  const [code, setCode] = useState('// Start coding...');
  const [output, setOutput] = useState('');
  const [versions, setVersions] = useState([]);
  const [selectedVersionId, setSelectedVersionId] = useState(null);
  const [onlineUsers, setOnlineUsers] = useState([]);
  const [folders, setFolders] = useState({});
  const [selectedFile, setSelectedFile] = useState(null);

  const editorRef = useRef(null);
  const isRemoteUpdate = useRef(false);
  const cursorMarkersRef = useRef({});
  const navigate = useNavigate();

  useEffect(() => {
    fetch('http://localhost:8080/auth/me', { credentials: 'include' })
      .then(res => {
        if (!res.ok) throw new Error('Unauthorized');
        return res.json();
      })
      .then(data => setUser(data))
      .catch(() => navigate('/'));
  }, [navigate]);

  useEffect(() => {
    if (selectedFile?.content !== undefined) {
      setCode(selectedFile.content);
    }
  }, [selectedFile]);

  useEffect(() => {
    if (!user) return;

    stompClient.onConnect = () => {
      stompClient.subscribe('/topic/code', (message) => {
        const body = JSON.parse(message.body);
        isRemoteUpdate.current = true;
        setCode(body.code);
      });

      stompClient.subscribe('/topic/cursor', (message) => {
        const { email, position } = JSON.parse(message.body);
        if (email !== user.email && editorRef.current) {
          showRemoteCursor(email, position);
        }
      });

      fetchVersions();
    };

    stompClient.activate();
    return () => stompClient.deactivate();
  }, [user]);

  useEffect(() => {
    if (!user) return;

    const fetchOnlineUsers = async () => {
      try {
        const res = await fetch('http://localhost:8080/users/online', {
          credentials: 'include'
        });
        const data = await res.json();
        if (Array.isArray(data)) {
          setOnlineUsers(data);
        } else {
          setOnlineUsers([]);
        }
      } catch {
        setOnlineUsers([]);
      }
    };

    fetchOnlineUsers();
    const interval = setInterval(fetchOnlineUsers, 3000);
    return () => clearInterval(interval);
  }, [user]);

  useEffect(() => {
    fetch("http://localhost:8080/files", { credentials: "include" })
      .then(res => res.json())
      .then((data) => {
        const grouped = {};
        data.forEach(file => {
          if (!grouped[file.folder]) grouped[file.folder] = {};
          grouped[file.folder][file.filename] = file.content;
        });
        setFolders(grouped);
      })
      .catch(() => {});
  }, []);

  const fetchVersions = async () => {
    const res = await fetch('http://localhost:8080/versions');
    const data = await res.json();
    setVersions(Array.isArray(data) ? data : []);
  };

  const handleCodeChange = (newValue) => {
    if (isRemoteUpdate.current) {
      isRemoteUpdate.current = false;
      return;
    }

    setCode(newValue);

    if (stompClient.connected && user?.role !== "VIEWER") {
      stompClient.publish({
        destination: '/app/edit',
        body: JSON.stringify({
          user: user?.email,
          code: newValue,
          timestamp: Date.now()
        })
      });
    }
  };

  const handleCursorChange = (editor) => {
    const pos = editor.getPosition();
    if (stompClient.connected && user) {
      stompClient.publish({
        destination: '/app/cursor',
        body: JSON.stringify({
          email: user.email,
          position: pos
        })
      });
    }
  };

  const showRemoteCursor = (email, pos) => {
    const editor = editorRef.current;
    if (!editor) return;

    const decorations = editor.deltaDecorations(
      cursorMarkersRef.current[email] || [],
      [{
        range: new window.monaco.Range(pos.lineNumber, pos.column, pos.lineNumber, pos.column),
        options: {
          className: 'ghost-cursor',
          hoverMessage: { value: email },
          beforeContentClassName: 'ghost-label'
        }
      }]
    );

    cursorMarkersRef.current[email] = decorations;
  };

  const handleRunCode = async () => {
    const res = await fetch('http://localhost:8080/run', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify({ language: 'cpp', code })
    });
    setOutput(await res.text());
  };

  const handleSaveVersion = async () => {
    await fetch('http://localhost:8080/versions/save', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        user: user?.email,
        code,
        timestamp: Date.now()
      })
    });
    await fetchVersions();
  };

  const handleRevertToSelected = async () => {
    if (selectedVersionId) {
      await fetch(`http://localhost:8080/versions/revert/${selectedVersionId}`, {
        method: 'POST'
      });
      setSelectedVersionId(null);
    }
  };

  const handleClearAllVersions = async () => {
    if (window.confirm("Delete all versions?")) {
      await fetch('http://localhost:8080/versions/clear', {
        method: 'DELETE'
      });
      await fetchVersions();
    }
  };

  if (!user) return <div>Loading...</div>;

  return (
    <div style={{ display: 'flex', height: '100vh', background: '#1e1e1e', color: 'white' }}>
      <div style={{ width: '300px', borderRight: '1px solid #333', padding: '1rem', overflowY: 'auto', display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
        <div>
          {user.role === 'ADMIN' && (
            <button onClick={() => navigate('/admin')} style={adminBtn}>
              Admin Privileges
            </button>
          )}
          <h3>Online Users</h3>
          <ul style={{ listStyle: 'none', padding: 0 }}>
            {onlineUsers.map((u, i) => (
              <li key={i} style={{ display: 'flex', alignItems: 'center', marginBottom: '1rem', paddingBottom: '0.5rem', borderBottom: '1px solid #444' }}>
                <img src={u.picture} alt={u.name} style={{ width: '30px', height: '30px', borderRadius: '50%', marginRight: '10px' }} />
                <div>
                  <strong>{u.name}</strong><br />
                  <span style={{ fontSize: '0.8rem', color: u.editing ? 'lightgreen' : 'gray' }}>
                    {u.editing ? 'editing now' : 'idle'}
                  </span>
                </div>
              </li>
            ))}
          </ul>
        </div>

        <Sidebar
          folders={folders}
          setFolders={setFolders}
          selectedFile={selectedFile}
          onFileSelect={(folder, file) => {
            const folderFiles = folders?.[folder];
            const content = folderFiles?.[file] ?? '';
            setSelectedFile({ folder, name: file, content });
          }}
        />
      </div>

      <div style={{ flexGrow: 1, padding: '1rem' }}>
        <h1>Welcome, {user.name} ({user.role})</h1>

        <Editor
          height="60vh"
          defaultLanguage="cpp"
          value={code}
          onChange={handleCodeChange}
          onMount={(editor) => {
            editorRef.current = editor;
            editor.onDidChangeCursorPosition(() => handleCursorChange(editor));
          }}
          theme="vs-dark"
          options={{ readOnly: user?.role === "VIEWER" }}
        />

        <div style={{ marginTop: '1rem' }}>
          <button onClick={handleRunCode} style={btn}>Run</button>
          <button onClick={handleSaveVersion} style={btn}>Save</button>
          {user.role === 'ADMIN' && <button onClick={handleClearAllVersions} style={btn}>Clear</button>}
          <select
            value={selectedVersionId || ''}
            onChange={(e) => setSelectedVersionId(e.target.value)}
            style={{ padding: '10px', marginRight: '1rem', background: '#333', color: 'white' }}
          >
            <option value="">Select version...</option>
            {versions.map(v => (
              <option key={v.id} value={v.id}>
                {new Date(v.timestamp).toLocaleString()} – {v.user}
              </option>
            ))}
          </select>
          <button onClick={handleRevertToSelected} style={btn}>Revert</button>
        </div>

        <div style={{ marginTop: '20px', padding: '10px', background: '#111', border: '1px solid #444', borderRadius: '4px', fontFamily: 'monospace', height: '20vh', overflowY: 'auto' }}>
          <strong>Output:</strong>
          <pre>{output}</pre>
        </div>
      </div>
    </div>
  );
}

const btn = {
  marginRight: '1rem',
  padding: '10px 20px',
  fontSize: '1rem',
  cursor: 'pointer',
  background: '#444',
  border: 'none',
  color: 'white'
};

const adminBtn = {
  marginBottom: '1rem',
  padding: '10px',
  background: '#333',
  color: 'white',
  border: '1px solid #555',
  width: '100%',
  cursor: 'pointer',
  fontWeight: 'bold'
};

export default MainEditor;
