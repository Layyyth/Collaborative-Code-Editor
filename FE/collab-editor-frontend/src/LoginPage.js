import React from 'react';

const LoginPage = () => {
  const handleLogin = () => {
    // Redirects to Spring Security OAuth2 Google login endpoint
    window.location.href = "http://localhost:8080/oauth2/authorization/google";
  };

  return (
    <div style={styles.container}>
      <h1 style={styles.title}>🔐 Collaborative Code Editor</h1>
      <p style={styles.sub}>Sign in with your Google account to get started</p>
      <button onClick={handleLogin} style={styles.button}>
        <img
          src="https://developers.google.com/identity/images/g-logo.png"
          alt="Google"
          style={styles.logo}
        />
        Sign in with Google
      </button>
    </div>
  );
};

const styles = {
  container: {
    height: '100vh',
    background: '#1e1e1e',
    color: 'white',
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
  },
  title: {
    fontSize: '2rem',
    marginBottom: '10px',
  },
  sub: {
    fontSize: '1rem',
    marginBottom: '20px',
    color: '#ccc',
  },
  button: {
    padding: '10px 20px',
    fontSize: '1rem',
    backgroundColor: 'white',
    color: 'black',
    border: 'none',
    borderRadius: '5px',
    display: 'flex',
    alignItems: 'center',
    gap: '10px',
    cursor: 'pointer',
    boxShadow: '0 2px 6px rgba(0,0,0,0.2)',
  },
  logo: {
    height: '20px',
    width: '20px',
  },
};

export default LoginPage;
