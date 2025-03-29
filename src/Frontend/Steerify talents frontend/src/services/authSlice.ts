import { createSlice } from '@reduxjs/toolkit';

interface AuthState {
    user: null | {
        id: string;
        email: string;
        firstName: string;
        lastName: string;
        role: string;
    };
    token: null | string;
}

const initialState: AuthState = {
    user: null,
    token: null,
};

export const authSlice = createSlice({
    name: 'auth',
    initialState,
    reducers: {
        setCredentials: (state, action) => {
            state.token = action.payload.token;
            state.user = {
                id: action.payload.userId,
                email: action.payload.email,
                firstName: action.payload.firstName,
                lastName: action.payload.lastName || action.payload.name?.split(' ')[1] || '',
                role: action.payload.role,
            };
            localStorage.setItem('token', action.payload.token);
            localStorage.setItem('user', JSON.stringify(state.user));
        },
        logout: (state) => {
            state.user = null;
            state.token = null;
            localStorage.removeItem('token');
            localStorage.removeItem('user');
        },
    },
});

export const { setCredentials, logout } = authSlice.actions;
export default authSlice.reducer;