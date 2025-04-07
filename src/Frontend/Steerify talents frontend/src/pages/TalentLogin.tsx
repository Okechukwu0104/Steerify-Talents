import {useState} from "react";
import {useForm} from "react-hook-form";
import {Link, useNavigate} from "react-router-dom";
import {motion} from "framer-motion";
import {ArrowLeft} from "lucide-react";
import {Button} from "@/components/ui/button";
import {Input} from "@/components/ui/input";
import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from "@/components/ui/form";
import {toast} from "@/hooks/use-toast";
import {useLoginTalentMutation, LoginRequestDto} from "@/services/talentApiSlice.ts";
import {useDispatch} from "react-redux";
import {setCredentials} from "@/store/authSlice";

interface TalentLoginProps {
    onBack?: () => void
}

const TalentLogin = ({onBack}: TalentLoginProps) => {
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const [loginTalent, {isLoading}] = useLoginTalentMutation();

    const form = useForm<LoginRequestDto>({
        defaultValues: {
            email: "",
            password: "",
        },
    });


    const onSubmit = async (data: LoginRequestDto) => {
        try {
            console.log("Attempting to login with:", data);

            const result = await loginTalent(data).unwrap();
            dispatch(setCredentials(result));
            localStorage.setItem('token', result.token);
            console.log("Stored token:", localStorage.getItem('token'));

            console.log("response:", result)

            toast({
                title: "Login Successful",
                description: `Welcome back, ${result.firstName}!`,
            });

            navigate("/dashboard", {state: {talent: result}});
        } catch (error) {
            console.error("Failed to login:", error);
            toast({
                title: "Login Failed",
                description: "Invalid email or password. Please try again.",
                variant: "destructive",
            });
        }
    };

    return (
        <div className="min-h-screen bg-neutral-100 flex flex-col">
            <div className="container mx-auto px-4 py-8">
                <Link
                    to="/"
                    className="inline-flex items-center text-neutral-600 hover:text-primary transition-colors"
                >
                    <ArrowLeft size={16} className="mr-2"/> Back to home
                </Link>
            </div>

            <div className="flex-1 flex items-center justify-center p-4">
                <motion.div
                    initial={{opacity: 0, y: 20}}
                    animate={{opacity: 1, y: 0}}
                    transition={{duration: 0.3}}
                    className="w-full max-w-md"
                >
                    <Card>
                        <CardHeader>
                            <CardTitle>Talent Sign In</CardTitle>
                            <CardDescription>
                                Access your talent profile and manage your opportunities
                            </CardDescription>
                        </CardHeader>
                        <CardContent>
                            <Form {...form}>
                                <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
                                    <FormField
                                        control={form.control}
                                        name="email"
                                        render={({field}) => (
                                            <FormItem>
                                                <FormLabel>Email</FormLabel>
                                                <FormControl>
                                                    <Input placeholder="mail@example.com" {...field} />
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        )}
                                    />
                                    <FormField
                                        control={form.control}
                                        name="password"
                                        render={({field}) => (
                                            <FormItem>
                                                <FormLabel>Password</FormLabel>
                                                <FormControl>
                                                    <Input type="password" placeholder="••••••••" {...field} />
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        )}
                                    />
                                    <Button type="submit" className="w-full" disabled={isLoading}>
                                        {isLoading ? "Signing in..." : "Sign in as Talent"}
                                    </Button>
                                </form>
                            </Form>
                        </CardContent>
                        <CardFooter className="flex flex-col space-y-4">
                            <div className="text-sm text-center w-full">
                                <Link to="/client-login" className="text-primary hover:underline">
                                    Sign in as Client instead
                                </Link>
                            </div>
                            <div className="text-sm text-center w-full">
                                Don't have an account?{" "}
                                <Link to="/talent-signup" className="text-primary hover:underline">
                                    Sign up as Talent
                                </Link>
                            </div>
                        </CardFooter>
                    </Card>
                </motion.div>
            </div>
        </div>
    );
};

export default TalentLogin;
