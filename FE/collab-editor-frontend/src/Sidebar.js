import React, { useState } from 'react';

function Sidebar({ folders, setFolders, selectedFile, onFileSelect }) {
  const [expandedFolder, setExpandedFolder] = useState(null);
  const [newFolderName, setNewFolderName] = useState('');
  const [newFileName, setNewFileName] = useState('');

  const handleCreateFolder = () => {
    if (!newFolderName.trim()) return;
    if (folders[newFolderName]) {
      alert("Folder already exists");
      return;
    }

    const updated = { ...folders, [newFolderName]: {} };
    setFolders(updated);
    setNewFolderName('');
  };

  const handleCreateFile = async () => {
    if (!expandedFolder || !newFileName.trim()) return;

    try {
      const res = await fetch("http://localhost:8080/files", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({
          folder: expandedFolder,
          filename: newFileName,
          content: ""
        })
      });

      if (!res.ok) throw new Error("Failed to create file");

      const updatedFolders = {
        ...folders,
        [expandedFolder]: {
          ...folders[expandedFolder],
          [newFileName]: ""
        }
      };

      setFolders(updatedFolders);
      setNewFileName('');
    } catch (err) {
      console.error("Error creating file:", err);
      alert("Failed to create file.");
    }
  };

  return (
    <div style={styles.sidebar}>
      <h3>📁 Files</h3>

      {/* Create New Folder */}
      <div style={styles.inputGroup}>
        <input
          placeholder="New folder"
          value={newFolderName}
          onChange={(e) => setNewFolderName(e.target.value)}
          style={styles.input}
        />
        <button onClick={handleCreateFolder} style={styles.btn}>➕</button>
      </div>

      {/* Create New File */}
      {expandedFolder && (
        <div style={styles.inputGroup}>
          <input
            placeholder="New file"
            value={newFileName}
            onChange={(e) => setNewFileName(e.target.value)}
            style={styles.input}
          />
          <button onClick={handleCreateFile} style={styles.btn}>📄</button>
        </div>
      )}

      {/* Folder & Files List */}
      <ul style={styles.list}>
        {Object.entries(folders).map(([folder, files]) => (
          <li key={folder}>
            <div
              onClick={() => setExpandedFolder(folder === expandedFolder ? null : folder)}
              style={styles.folder}
            >
              📂 {folder}
            </div>
            {expandedFolder === folder && (
              <ul style={styles.fileList}>
                {Object.keys(files).map(file => (
                  <li
                    key={file}
                    onClick={() => onFileSelect(folder, file)}
                    style={{
                      ...styles.file,
                      fontWeight:
                        selectedFile?.folder === folder && selectedFile?.name === file
                          ? 'bold'
                          : 'normal'
                    }}
                  >
                    📄 {file}
                  </li>
                ))}
              </ul>
            )}
          </li>
        ))}
      </ul>
    </div>
  );
}

const styles = {
  sidebar: {
    marginTop: '2rem',
    color: 'white',
    padding: '10px',
    borderTop: '1px solid #444'
  },
  inputGroup: {
    display: 'flex',
    marginBottom: '0.5rem'
  },
  input: {
    flexGrow: 1,
    background: '#222',
    border: '1px solid #555',
    color: 'white',
    padding: '5px',
    marginRight: '5px'
  },
  btn: {
    background: '#444',
    border: 'none',
    color: 'white',
    padding: '5px 10px',
    cursor: 'pointer'
  },
  list: {
    listStyle: 'none',
    paddingLeft: 0,
    marginTop: '1rem'
  },
  folder: {
    cursor: 'pointer',
    fontWeight: 'bold',
    marginBottom: '0.3rem'
  },
  fileList: {
    marginLeft: '1rem'
  },
  file: {
    cursor: 'pointer',
    padding: '3px 0'
  }
};

export default Sidebar;
