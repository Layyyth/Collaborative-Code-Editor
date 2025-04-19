import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

function AdminDashboard() {
  const [users, setUsers] = useState([]);
  const [currentUser, setCurrentUser] = useState(null);
  const navigate = useNavigate();

  const fetchUsers = async () => {
    try {
      const res = await fetch('http://localhost:8080/admin/users', { credentials: 'include' });
      if (!res.ok) throw new Error("Unauthorized or failed to fetch users");
      const data = await res.json();
      setUsers(data);
    } catch (err) {
      console.error("❌ Error fetching users:", err);
      navigate('/'); // fallback redirect
    }
  };

  const updateRole = async (email, role) => {
    const normalizedRole = role?.toString().toUpperCase(); // ✅ make sure role is uppercase
    try {
      const res = await fetch(`http://localhost:8080/admin/users/${encodeURIComponent(email)}/role`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        },
        credentials: 'include',
        body: JSON.stringify({ role: normalizedRole })
      });

      if (res.ok) {
        await fetchUsers();
      } else {
        const errorText = await res.text();
        alert("❌ Failed to update role: " + errorText);
      }
    } catch (err) {
      alert("❌ Error updating role: " + err.message);
    }
  };

  useEffect(() => {
    let intervalId;

    const init = async () => {
      try {
        const res = await fetch('http://localhost:8080/auth/me', { credentials: 'include' });
        if (!res.ok) throw new Error('Unauthorized');
        const user = await res.json();

        if (user.role !== 'ADMIN') {
          navigate('/editor');
        } else {
          setCurrentUser(user);
          await fetchUsers();
          intervalId = setInterval(fetchUsers, 5000);
        }
      } catch (err) {
        navigate('/');
      }
    };

    init();
    return () => clearInterval(intervalId);
  }, [navigate]);

  return (
    <div style={{ padding: '2rem', color: 'white', background: '#121212', minHeight: '100vh' }}>
      <h1>🛠 Admin Dashboard</h1>
      <table style={{ width: '100%', borderCollapse: 'collapse', color: 'white' }}>
        <thead>
          <tr style={{ borderBottom: '1px solid #444' }}>
            <th style={th}>Name</th>
            <th style={th}>Email</th>
            <th style={th}>Role</th>
            <th style={th}>Online</th>
            <th style={th}>⌨ Editing</th>
            <th style={th}>Last Seen</th>
            <th style={th}>Actions</th>
          </tr>
        </thead>
        <tbody>
          {users.map((u, i) => (
            <tr key={i} style={{ borderBottom: '1px solid #333' }}>
              <td style={td}>{u.name}</td>
              <td style={td}>{u.email}</td>
              <td style={td}>{u.role}</td>
              <td style={td}>{u.online ? '✅' : '❌'}</td>
              <td style={td}>{u.editing ? '💻' : ''}</td>
              <td style={td}>{new Date(u.lastSeen).toLocaleString()}</td>
              <td style={td}>
                <select
                  value={u.role}
                  onChange={(e) => updateRole(u.email, e.target.value)}
                  style={select}
                >
                  <option value="USER">USER</option>
                  <option value="ADMIN">ADMIN</option>
                  <option value="VIEWER">VIEWER</option>
                </select>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

const th = { padding: '10px', textAlign: 'left' };
const td = { padding: '10px', textAlign: 'left' };
const select = {
  padding: '6px',
  background: '#333',
  color: 'white',
  border: '1px solid #666',
  borderRadius: '4px'
};

export default AdminDashboard;
