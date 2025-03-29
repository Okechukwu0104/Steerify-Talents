
import { Outlet } from "react-router-dom";
import Navigation from "../landing/Navigation";
import Footer from "../landing/Footer";
import { Toaster } from "@/components/ui/toaster";
import { Toaster as Sonner } from "@/components/ui/sonner";
import { TooltipProvider } from "@/components/ui/tooltip";

const Layout = () => {
  return (
    <TooltipProvider>
      <Toaster />
      <Sonner />
      <div className="min-h-screen flex flex-col">
        <Navigation />
        <main className="flex-1">
          <Outlet />
        </main>
        {/*<Footer />*/}
      </div>
    </TooltipProvider>
  );
};

export default Layout;
