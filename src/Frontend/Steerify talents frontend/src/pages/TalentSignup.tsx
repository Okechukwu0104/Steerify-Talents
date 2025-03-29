
import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { Link, useNavigate } from "react-router-dom";
import { motion } from "framer-motion";
import { ArrowLeft, User, Plus, X } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
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
import { toast } from "@/hooks/use-toast";
import { useRegisterTalentMutation, TalentSignupDto } from "@/services/talentApiSlice.ts";
import { Badge } from "@/components/ui/badge";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

const TalentSignup = () => {
  const navigate = useNavigate();
  const [registerTalent, { isLoading }] = useRegisterTalentMutation();
  const [skillInput, setSkillInput] = useState("");
  const [skillsList, setSkillsList] = useState<string[]>([]);
  
  const form = useForm<TalentSignupDto>({
    defaultValues: {
      firstName: "",
      lastName: "",
      email: "",
      password: "",
      phoneNumber: "",
      skills: [],
      gender: "",
      education: "",
      address: "",
        age:0,
    },
  });

  const addSkill = () => {
    if (skillInput.trim() && !skillsList.includes(skillInput.trim())) {
      const updatedSkills = [...skillsList, skillInput.trim()];
      setSkillsList(updatedSkills);
      form.setValue('skills', updatedSkills);
      setSkillInput("");
    }
  };

  const removeSkill = (skill: string) => {
    const updatedSkills = skillsList.filter(s => s !== skill);
    setSkillsList(updatedSkills);
    form.setValue('skills', updatedSkills);
  };

  const handleKeyPress = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter') {
      e.preventDefault();
      addSkill();
    }
  };

  const onSubmit = async (data: TalentSignupDto) => {
    // Make sure skills are included
    const formData = {
      ...data,
      skills: skillsList
    };

    try {
      console.log("Submitting talent data:", formData);
      const response = await registerTalent(formData).unwrap();
      console.log("the response   ",response)
      
      toast({
        title: "Registration Successful",
        description: "Your talent account has been created. Please login.",
      });
      
      navigate("/talent-login");
    } catch (error) {
      console.error("Failed to register:", error);
      toast({
        title: "Registration Failed",
        description: "There was an error creating your account. Please try again.",
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
          <ArrowLeft size={16} className="mr-2" /> Back to home
        </Link>
      </div>
      
      <div className="flex-1 flex items-center justify-center p-4">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.3 }}
          className="w-full max-w-2xl"
        >
          <Card>
            <CardHeader className="space-y-1">
              <div className="flex items-center gap-2">
                <User className="h-5 w-5 text-primary" />
                <CardTitle>Create a Talent Profile</CardTitle>
              </div>
              <CardDescription>
                Sign up as talent to showcase your skills and find opportunities
              </CardDescription>
            </CardHeader>
            <CardContent>
              <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
                  <div className="grid grid-cols-2 gap-4">
                    <FormField
                      control={form.control}
                      name="firstName"
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>First Name*</FormLabel>
                          <FormControl>
                            <Input placeholder="John" {...field} required />
                          </FormControl>
                          <FormMessage />
                        </FormItem>
                      )}
                    />


                    <FormField
                      control={form.control}
                      name="lastName"
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>Last Name*</FormLabel>
                          <FormControl>
                            <Input placeholder="Doe" {...field} required />
                          </FormControl>
                          <FormMessage />
                        </FormItem>
                      )}
                    />
                  </div>

                  <div className="grid grid-cols-2 gap-4">



                    <FormField
                      control={form.control}
                      name="gender"
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>Gender*</FormLabel>
                          <Select
                            onValueChange={field.onChange}
                            defaultValue={field.value}
                          >
                            <FormControl>
                              <SelectTrigger>
                                <SelectValue placeholder="Select gender" />
                              </SelectTrigger>
                            </FormControl>
                            <SelectContent>
                              <SelectItem value="male">Male</SelectItem>
                              <SelectItem value="female">Female</SelectItem>
                              <SelectItem value="other">Other</SelectItem>
                            </SelectContent>
                          </Select>
                          <FormMessage />
                        </FormItem>
                      )}
                    />
                    <FormField
                      control={form.control}
                      name="email"
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>Email*</FormLabel>
                          <FormControl>
                            <Input type="email" placeholder="mail@example.com" {...field} required />
                          </FormControl>
                          <FormMessage />
                        </FormItem>
                      )}
                    />
                  </div>
                    <FormField
                        control={form.control}
                        name="age"
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>Age*</FormLabel>
                                <FormControl>
                                    <Input
                                        type="number"
                                        min="18"
                                        max="100"
                                        className="w-20" // Makes the input narrower
                                        placeholder="25"
                                        {...field}
                                        onChange={(e) => field.onChange(parseInt(e.target.value))}
                                        required
                                    />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                  <FormField
                    control={form.control}
                    name="education"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Education*</FormLabel>
                        <FormControl>
                          <Input placeholder="Your education background" {...field} required />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />

                  <FormField
                    control={form.control}
                    name="address"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Address</FormLabel>
                        <FormControl>
                          <Input placeholder="Your address" {...field} />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />

                  <FormField
                    control={form.control}
                    name="phoneNumber"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Phone Number*</FormLabel>
                        <FormControl>
                          <Input placeholder="+1234567890" {...field} required />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />

                  <FormField
                    control={form.control}
                    name="skills"
                    render={() => (
                      <FormItem>
                        <FormLabel>Skills* (at least one is required)</FormLabel>
                        <div className="flex gap-2">
                          <Input
                            placeholder="Enter a skill and press Enter or Add"
                            value={skillInput}
                            onChange={(e) => setSkillInput(e.target.value)}
                            onKeyDown={handleKeyPress}
                          />
                          <Button
                            type="button"
                            size="sm"
                            onClick={addSkill}
                            variant="outline"
                          >
                            <Plus size={16} />
                          </Button>
                        </div>

                        <div className="flex flex-wrap gap-2 mt-2">
                          {skillsList.map((skill, index) => (
                            <Badge key={index} variant="secondary" className="px-3 py-1">
                              {skill}
                              <button
                                type="button"
                                className="ml-1 text-muted-foreground hover:text-foreground"
                                onClick={() => removeSkill(skill)}
                              >
                                <X size={14} />
                              </button>
                            </Badge>
                          ))}
                        </div>
                        {skillsList.length === 0 && (
                          <p className="text-sm text-muted-foreground mt-1">Add at least one skill</p>
                        )}
                        <FormMessage />
                      </FormItem>
                    )}
                  />

                  <FormField
                    control={form.control}
                    name="password"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Password*</FormLabel>
                        <FormControl>
                          <Input type="password" placeholder="••••••••" {...field} required />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />

                  <Button
                    type="submit"
                    className="w-full"
                    disabled={isLoading || skillsList.length === 0}
                  >
                    {isLoading ? "Creating Profile..." : "Create Talent Profile"}
                  </Button>
                </form>
              </Form>
            </CardContent>
            <CardFooter className="flex flex-col space-y-4">
              <div className="text-sm text-center w-full">
                <Link to="/client-signup" className="text-primary hover:underline">
                  Sign up as Client instead
                </Link>
              </div>
              <div className="text-sm text-center w-full">
                Already have an account?{" "}
                <Link to="/talent-login" className="text-primary hover:underline">
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

export default TalentSignup;
