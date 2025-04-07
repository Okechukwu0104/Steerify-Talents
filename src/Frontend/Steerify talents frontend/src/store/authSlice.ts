
import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { LoginResponseDto } from '../services/talentApiSlice.ts';

interface AuthState {
  token: string | null;
  user: {
    userId: string | null;
    name: string | null;
    email: string | null;
    role: string | null;
  };
  isAuthenticated: boolean;
}

const initialState: AuthState = {
  token: localStorage.getItem('token'),
  user: {
    userId: localStorage.getItem('userId'),
    name: localStorage.getItem('name'),
    email: localStorage.getItem('email'),
    role: localStorage.getItem('role'),
  },
  isAuthenticated: !!localStorage.getItem('token'),
};

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    setCredentials: (state, action: PayloadAction<LoginResponseDto>) => {
      const { token, userId, name, email, role } = action.payload;
      
      state.token = token;
      state.user.userId = userId;
      state.user.name = name;
      state.user.email = email;
      state.user.role = role;
      state.isAuthenticated = true;
      
      localStorage.setItem('token', token);
      localStorage.setItem('userId', userId);
      localStorage.setItem('name', name);
      localStorage.setItem('email', email);
      localStorage.setItem('role', role);
    },
    logout: (state) => {
      state.token = null;
      state.user = {
        userId: null,
        name: null,
        email: null,
        role: null,
      };
      state.isAuthenticated = false;
      
      localStorage.removeItem('token');
      localStorage.removeItem('userId');
      localStorage.removeItem('name');
      localStorage.removeItem('email');
      localStorage.removeItem('role');
    },
  },
});

export const { setCredentials, logout } = authSlice.actions;
export default authSlice.reducer;

export const selectCurrentUser = (state: { auth: AuthState }) => state.auth.user;
export const selectIsAuthenticated = (state: { auth: AuthState }) => state.auth.isAuthenticated;
export const selectUserRole = (state: { auth: AuthState }) => state.auth.user.role;
