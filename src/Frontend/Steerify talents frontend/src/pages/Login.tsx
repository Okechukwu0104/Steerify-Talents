import { useState } from "react";
import { Link } from "react-router-dom";
import { motion } from "framer-motion";
import { ArrowLeft, User, Briefcase } from "lucide-react";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import TalentLogin from "@/pages/TalentLogin.tsx";
import ClientLogin from "@/pages/ClientLogin.tsx";

const Login = () => {
  const [loginType, setLoginType] = useState<"talent" | "client" | null>(null);

  if (loginType === "talent") {
    localStorage.setItem('userRole', 'talent');
    return <TalentLogin onBack={() => setLoginType(null)} />;

  }

  if (loginType === "client") {
    localStorage.setItem('userRole', 'client');
    return <ClientLogin onBack={() => setLoginType(null)} />;

  }

  return (
      <div className="min-h-screen bg-neutral-100 flex flex-col">
        <div className="container mx-auto px-4 py-8">
          <Link
              to="/"
              className="inline-flex items-center text-neutral-600 hover:text-primary transition-colors"
          >
            <ArrowLeft size={16} className="mr-2" /> Back to home
          </Link>
        </div>

        <div className="flex-1 flex items-center justify-center p-4">
          <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.3 }}
              className="w-full max-w-md"
          >
            <Card>
              <CardHeader>
                <CardTitle>Welcome to Steerify Talents</CardTitle>
                <CardDescription>
                  Choose how you want to sign in
                </CardDescription>
              </CardHeader>
              <CardContent className="space-y-4">
                <Button
                    onClick={() => setLoginType("talent")}
                    variant="outline"
                    className="w-full h-16 justify-start text-left"
                >
                  <User className="mr-4" />
                  <div>
                    <div className="font-medium">Sign in as Talent</div>
                    <div className="text-sm text-muted-foreground">
                      For performers, artists, and creative professionals
                    </div>
                  </div>
                </Button>
                <Button
                    onClick={() => setLoginType("client")}
                    variant="outline"
                    className="w-full h-16 justify-start text-left"
                >
                  <Briefcase className="mr-4" />
                  <div>
                    <div className="font-medium">Sign in as Client</div>
                    <div className="text-sm text-muted-foreground">
                      For agencies, casting directors, and project managers
                    </div>
                  </div>
                </Button>
              </CardContent>
              <CardFooter className="flex justify-center">
                <div className="text-sm text-center">
                  Don't have an account?{" "}
                  <Link to="/signup" className="text-primary hover:underline">
                    Sign up
                  </Link>
                </div>
              </CardFooter>
            </Card>
          </motion.div>
        </div>
      </div>
  );
};

export default Login;