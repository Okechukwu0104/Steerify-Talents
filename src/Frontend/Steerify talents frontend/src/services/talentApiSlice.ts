
import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import {Key, ReactNode} from "react";

// Define types for API responses and requests
export interface LoginRequestDto {
  email: string;
  password: string;
}

export interface LoginResponseDto {
  firstName: any;
  token: string;
  userId: string;
  name: string;
  email: string;
  role: string;
}

export interface TalentSignupDto {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  phoneNumber: string;
  skills: string[]; // Updated to array of strings
  gender: string;
  age?: number;
  address?: string;
  education: string;
}

export interface ClientSignupDto {
  companyName: string;
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  phone: string;
  contactPerson: string;
  industry: string;
  description: string;
}

export interface PostDto {
  phoneNumber: ReactNode;
  price: ReactNode;
  description: ReactNode;
  name: Key;
  tags: any;
  createdAt: string | number | Date;
  author: any;
  id: Key;
  featured: any;
  content: string;
  title: string;
  role:string;
}

export interface JobDto {
  jobId: string;
  clientId: string;
  clientName: string;
  title: string;
  description: string;
  requiredSkills: string[];
  location: string;
  payment: string;
  deadline: string;
}


export interface ApplicationDto {
  cover: string;
  jobId: string;
}

// Create the API slice
export const talentApiSlice = createApi({
  reducerPath: 'talentApi',
  baseQuery: fetchBaseQuery({
    baseUrl: 'http://localhost:1101/api',
    prepareHeaders: (headers, { getState }) => {
      // Get token from state
      const token = (getState() as any).auth.token;
      
      // If token exists, add it to the headers
      if (token) {
        headers.set('Authorization', `Bearer ${token}`);
      }
      
      return headers;
    }
  }),
  endpoints: (builder) => ({
    // Talent endpoints
    loginTalent: builder.mutation<LoginResponseDto, LoginRequestDto>({
      query: (credentials) => ({
        url: 'api/talents/login',
        method: 'POST',
        body: credentials,
      }),
    }),

    registerTalent: builder.mutation<TalentSignupDto, TalentSignupDto>({
      query: (talentData) => ({
        url: 'api/talents/signup',
        method: 'POST',
        body: talentData,
      }),
    }),
    getTalentJobs: builder.query<JobDto[], void>({
      query: () => 'api/talents/jobs',
    }),


    createTalentPost: builder.mutation<PostDto, { talentId: string; postData: PostDto }>({
      query: ({ talentId, postData }) => ({
        url: `api/talents/${talentId}/post`,
        method: 'POST',
        body: postData,
      }),
    }),
    getTalentPosts: builder.query<PostDto[], void>({
      query: () => 'api/talents/posts',
    }),
    applyForJob: builder.mutation<ApplicationDto, { jobId: string; applicationData: ApplicationDto }>({
      query: ({ jobId, applicationData }) => ({
        url: `api/talents/apply/${jobId}`,
        method: 'POST',
        body: applicationData,
      }),
    }),






    // Client endpoints
    loginClient: builder.mutation<LoginResponseDto, LoginRequestDto>({
      query: (credentials) => ({
        url: 'api/clients/login',
        method: 'POST',
        body: credentials,
      }),
    }),
    registerClient: builder.mutation<ClientSignupDto, ClientSignupDto>({
      query: (clientData) => ({
        url: 'api/clients/signup',
        method: 'POST',
        body: clientData,
      }),
    }),
  }),
});

// Export the auto-generated hooks
export const {
  useLoginTalentMutation,
  useRegisterTalentMutation,
  useGetTalentJobsQuery,
  useCreateTalentPostMutation,
  useGetTalentPostsQuery,
  useApplyForJobMutation,
  useLoginClientMutation,
  useRegisterClientMutation,
} = talentApiSlice;
