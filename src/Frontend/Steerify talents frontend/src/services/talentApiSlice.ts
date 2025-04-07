
import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import {Key, ReactNode} from "react";


export interface TalentDto {
  id?: string ;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber?: string;
  address?: string;
  education?: string;
  gender?: string;
  skills?: string[];
  password?: string;
}

export interface ClientDto {
  id?: string ;
  companyName: string;
  firstName: string;
  lastName: string;
  description?: string;
  contactPerson?: string;
  phone?: string;
  password?: string;
}

export interface LoginRequestDto {
  email: string;
  password: string;
}

export interface LoginResponseDto {
  companyName: string;
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
  skills: string[];
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
  postId: Key;
  phoneNumber: ReactNode;
  price: ReactNode;
  description: ReactNode;
  name: Key;
  tags: any;
  createdAt: string | number | Date;
  author: any;
  id: Key | string;
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

export interface CommentDto {
  id: string;
  postId: string;
  userId: string;
  text: string;
  authorName: string;
  createdAt: string;
}

export interface CreateCommentDto {
  postId: string;
  userId: string;
  text: string;
  authorName: string;
}


export interface ApplicationDto {
  applicationId: string;
  talentId: string;
  jobId: string;
  coverLetter: string;
  stats: string;
  createdAt: string;
}


interface CreateJobDto {
  title: string;
  description: string;
  requiredSkills: string[];
  location: string;
  payment: string;
  deadline: string;
}

export const talentApiSlice = createApi({
  reducerPath: 'talentApi',
  baseQuery: fetchBaseQuery({
    baseUrl: 'http://localhost:1101/api',
    prepareHeaders: (headers, { getState }) => {
      const token = (getState() as any).auth.token;

      if (token) {
        headers.set('Authorization', `Bearer ${token}`);
      }

      return headers;
    }
  }),
  endpoints: (builder) => ({
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
    getJobs: builder.query<JobDto[], void>({
      query: () => 'api/talents/jobs',
    }),



    createTalentPost: builder.mutation<PostDto, { talentId: string; postData: PostDto }>({
      query: ({ talentId, postData }) => ({
        url: `api/talents/${talentId}/post`,
        method: 'POST',
        body: postData,
      }),
    }),

    allCreatedJobsByClient: builder.query({
      query: (clientId)=> `api/clients/${clientId}/jobs`
    }),

    getTalentPosts: builder.query<PostDto[], void>({
      query: () => 'api/talents/posts',
    }),

    getPostComments: builder.query<CommentDto[], string>({
      query: (postId) => `api/talents/posts/${postId}/comments`,
    }),






    getClientDetails: builder.query({
      query: (clientId) => `api/clients/about-client/${clientId}`,
    }),

    getTalentDetails: builder.query({
      query: (talentId)=> `api/talents/about-talent/${talentId}`,
    }),







    getApplicationsByJobId: builder.query({
      query:(jobId)=> `api/clients/application/${jobId}`,
    }),




    updateApplicationStatus: builder.mutation({
      query: ({ applicationId, status }) => ({
        url: `api/clients/application/${applicationId}/status`,
        method: 'PUT',
        params: { status },
      }),
    }),

    getJobAppPerOffer: builder.query({
      query:(jobId)=> `api/clients/${jobId}/jobs`
    }),











    updateJob: builder.mutation<JobDto, {
      jobId: string;
      jobData: JobDto
    }>({
      query: ({ jobId, jobData }) => ({
        url: `api/clients/jobs/${jobId}`,
        method: 'PUT',
        body: jobData,
      }),
    }),

    deleteJob: builder.mutation<void, string>({
      query: (jobId) => ({
        url: `api/clients/delete-job/${jobId}`,
        method: 'DELETE',
      }),
    }),


    updateTalent: builder.mutation<TalentDto, { talentId: string; talentDto: TalentDto }>({
      query: ({ talentId, talentDto }) => ({
        url: `/talents/${talentId}`,
        method: 'PUT',
        body: talentDto,
      }),
    }),
    deleteTalent: builder.mutation<void, string>({
      query: (talentId) => ({
        url: `api/talents/${talentId}`,
        method: 'DELETE',
      }),
    }),

    updateClient: builder.mutation<ClientDto, { clientId: string; clientDto: ClientDto }>({
      query: ({ clientId, clientDto }) => ({
        url: `/clients/update/${clientId}`,
        method: 'PUT',
        body: clientDto,
      }),
    }),

    deleteClient: builder.mutation<void, string>({
      query: (clientId) => ({
        url: `/clients/${clientId}`,
        method: 'DELETE',
      }),
    }),











    yourApplications: builder.query({
      query:(talentId)=> `api/talents/${talentId}/applications`
    }),

    deleteApplication: builder.mutation<void, { talentId: string; applicationId: string }>({
      query: ({ talentId, applicationId }) => ({
        url: `api/talents/${talentId}/${applicationId}`,
        method: 'DELETE',
      }),
    }),

    applyForJob: builder.mutation<ApplicationDto, { talentId: string, jobId: string; applicationData: ApplicationDto }>({
      query: ({ talentId, jobId, applicationData }) => ({
        url: `api/talents/${talentId}/apply/${jobId}`,
        method: 'POST',
        body: applicationData,
      }),

    }),


    addComment: builder.mutation<CommentDto, CreateCommentDto>({
      query: (comment) => ({
        url: `api/comments`,
        method: 'POST',
        body: comment,
      }),
    }),



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

    addJobOffer: builder.mutation<JobDto, { clientId: string; jobData: CreateJobDto }>({
      query: ({ clientId, jobData }) => ({
        url: `api/clients/${clientId}/jobs/create`,
        method: 'POST',
        body: jobData,

      }),
    }),






  }),
});

export const {
  useLoginTalentMutation,
  useRegisterTalentMutation,
  useGetJobsQuery,
  useCreateTalentPostMutation,
  useGetTalentPostsQuery,
  useGetClientDetailsQuery,
  useApplyForJobMutation,
  useLoginClientMutation,
  useRegisterClientMutation,
  useDeleteApplicationMutation,
    useYourApplicationsQuery,
    useAddJobOfferMutation,
    useGetApplicationsByJobIdQuery,
    useUpdateApplicationStatusMutation,
    useGetJobAppPerOfferQuery,
    useAllCreatedJobsByClientQuery,
    useUpdateJobMutation,
    useDeleteJobMutation,
    useGetTalentDetailsQuery,

  useDeleteTalentMutation,
  useUpdateTalentMutation,
  useDeleteClientMutation,
  useUpdateClientMutation
} = talentApiSlice;
