
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
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

type UserType = "talent" | "client";

const Signup = () => {
  const [userType, setUserType] = useState<UserType | null>(null);
  const navigate = useNavigate();

  const handleContinue = () => {
    if (userType === "talent") {
      navigate("/talent-signup");
    } else if (userType === "client") {
      navigate("/client-signup");
    }
  };

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
              <CardTitle>Create a Steerify Account</CardTitle>
              <CardDescription>
                Choose the type of account you want to create
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              <Button 
                onClick={() => setUserType("talent")}
                variant={userType === "talent" ? "default" : "outline"} 
                className="w-full h-16 justify-start text-left"
              >
                <User className="mr-4" />
                <div>
                  <div className="font-medium">Sign up as Talent</div>
                  <div className="text-sm text-muted-foreground">For performers, artists, and creative professionals</div>
                </div>
              </Button>
              <Button 
                onClick={() => setUserType("client")}
                variant={userType === "client" ? "default" : "outline"} 
                className="w-full h-16 justify-start text-left"
              >
                <Briefcase className="mr-4" />
                <div>
                  <div className="font-medium">Sign up as Client</div>
                  <div className="text-sm text-muted-foreground">For agencies, casting directors, and project managers</div>
                </div>
              </Button>
              
              {userType && (
                <div className="pt-4">
                  <Button className="w-full" onClick={handleContinue}>
                    Continue as {userType === "talent" ? "Talent" : "Client"}
                  </Button>
                  <p className="text-xs text-center mt-2 text-muted-foreground">
                    By continuing, you agree to our Terms of Service and Privacy Policy
                  </p>
                </div>
              )}
            </CardContent>
            <CardFooter className="flex justify-center">
              <div className="text-sm text-center">
                Already have an account?{" "}
                <Link to="/login" className="text-primary hover:underline">
                  Sign in
                </Link>
              </div>
            </CardFooter>
          </Card>
        </motion.div>
      </div>
    </div>
  );
};

export default Signup;
