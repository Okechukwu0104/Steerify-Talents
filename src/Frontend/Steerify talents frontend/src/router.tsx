
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
      {path: "talent-jobs", element: <JobsPage/>}
    ]
  }
]);

export default router;
