
import { createBrowserRouter } from "react-router-dom";
import Layout from "./components/layout/Layout";
import Index from "./pages/Index";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import ClientLogin from "./pages/ClientLogin";
import TalentLogin from "./pages/TalentLogin";
import ClientSignup from "./pages/ClientSignup";
import TalentSignup from "./pages/TalentSignup";
import TalentDashboard from "./pages/TalentDashboard.tsx";
import PostsPage from "@/pages/PostsPage.tsx";
import {JobsPage} from "@/pages/JobsPage.tsx";
import AboutPage from "@/pages/AboutPage.tsx";
import ApplicationsPage from "@/pages/ApplicationsPage.tsx";
import ApplicationDetails from "./pages/ApplicationDetails.tsx";
import JobOffers from "./pages/JobOffers.tsx";
import ApplicantsPage from "@/pages/ApplicantsPage.tsx";
import SettingsPage from "./pages/ClientSettingsPage.tsx";
// @ts-ignore
const router = createBrowserRouter([
  {
    path: "/",
    element: <Layout />,
    children: [
      { index: true, element: <Index /> },
      { path: "login", element: <Login /> },
      { path: "signup", element: <Signup /> },
      { path: "client-login", element: <ClientLogin /> },
      { path: "talent-login", element: <TalentLogin /> },
      { path: "client-signup", element: <ClientSignup /> },
      { path: "talent-signup", element: <TalentSignup /> },
      {path: "dashboard", element: <TalentDashboard />},
      {path: "posts", element: <PostsPage />},
      {path: "talent-jobs", element: <JobsPage />},
      {path: "about", element: <AboutPage />},
      {path: "applications", element: <ApplicationsPage />},
      {path: "/applications/:applicationId", element: <ApplicationDetails />},
      {path: "my-offers", element: <JobOffers />},
      {path: "applicants", element: <ApplicantsPage />},
      {path: "settings", element: <SettingsPage/>},
    ]
  }
]);

export default router;
