
import { configureStore } from '@reduxjs/toolkit';
import { talentApiSlice } from '../services/talentApiSlice.ts';
import { setupListeners } from '@reduxjs/toolkit/query';
import authReducer from './authSlice';

export const store = configureStore({
  reducer: {
    [talentApiSlice.reducerPath]: talentApiSlice.reducer,
    auth: authReducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware().concat(talentApiSlice.middleware),
});

// Enable listeners for automatic refetching
setupListeners(store.dispatch);

// Export types
export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
