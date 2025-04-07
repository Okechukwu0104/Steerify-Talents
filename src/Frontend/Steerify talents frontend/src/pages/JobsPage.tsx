import React, { useState } from "react";
import {useAddJobOfferMutation, useApplyForJobMutation} from "@/services/talentApiSlice";
import { useGetJobsQuery } from "@/services/talentApiSlice";
import { Card } from "@/components/ui/card";
import { useNavigate, useLocation } from "react-router-dom";
import { Skeleton } from "@/components/ui/skeleton";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { AlertCircle, Search, Bookmark, MapPin, DollarSign, Clock, Plus, Briefcase } from "lucide-react";
import { Avatar, AvatarFallback, AvatarImage } from "@radix-ui/react-avatar";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";

import {
    Dialog,
    DialogContent,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
    DialogDescription
} from "@/components/ui/dialog";
import {toast} from "@/hooks/use-toast";


interface Job {
    jobId: string;
    title: string;
    description: string;
    clientName: string;
    location: string;
    payment: string;
    deadline: string;
    requiredSkills: string[];
    clientId: string;
}

export const JobsPage = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const { data: jobs, isLoading, isError, error } = useGetJobsQuery();
    const [searchTerm, setSearchTerm] = useState("");
    const isClient = localStorage.getItem('userRole') === 'client';
    const [selectedJobId, setSelectedJobId] = useState<string | null>(null);
    const [applicationCover, setApplicationCover] = useState("");
    const [applyForJob] = useApplyForJobMutation();
    const client = location.state?.talent

    const normalizePayment = (payment: string): string => {
        return payment.toLowerCase().replace(/[^0-9]/g, '');
    };

    const filteredJobs = jobs?.filter((job: Job) => {
        const paymentNumeric = normalizePayment(job.payment);
        if (!searchTerm) return true;
        const searchLower = searchTerm.toLowerCase();
        const deadlineStr = new Date(job.deadline).toLocaleDateString().toLowerCase();




        return (
            job.title.toLowerCase().includes(searchLower) ||
            job.description.toLowerCase().includes(searchLower) ||
            job.clientName.toLowerCase().includes(searchLower) ||
            job.location.toLowerCase().includes(searchLower) ||
            paymentNumeric.includes(searchLower) ||
            deadlineStr.includes(searchLower) ||
            job.requiredSkills.some(skill => skill.toLowerCase().includes(searchLower))
        );
    }) || [];

    if (isLoading) {
        return (
            <div className="container mx-auto py-8 mt-[8rem]">
                <div className="grid gap-6">
                    {[...Array(3)].map((_, i) => (
                        <Skeleton key={i} className="h-48 w-full rounded-lg" />
                    ))}
                </div>
            </div>
        );
    }

    if (isError) {
        return (
            <div className="container mx-auto py-8 mt-[8rem]">
                <Alert variant="destructive">
                    <AlertCircle className="h-4 w-4" />
                    <AlertTitle>Error</AlertTitle>
                    <AlertDescription>
                        {error?.toString() || 'Failed to load jobs. Please try again later.'}
                    </AlertDescription>
                </Alert>
            </div>
        );
    }

    const talentId = location.state?.talent?.id || localStorage.getItem('userId');

    const handleApply = async () => {
        if (!selectedJobId || !applicationCover.trim()) return;

        if (!talentId) {
            navigate('/login');
            return;
        }

        try {
            await applyForJob({
                talentId,
                jobId: selectedJobId,
                applicationData: {
                    applicationId: "",
                    talentId: talentId,
                    jobId: selectedJobId,
                    coverLetter: applicationCover,
                    stats: "PENDING",
                    createdAt: ""
                }
            }).unwrap();

            setSelectedJobId(null);
            setApplicationCover("");
            toast({
                title: "Application submitted",
                description: "Your application was submitted successfully!",
            });

        } catch (error) {
            console.error("Failed to apply:", error);
            toast({
                title: "Error",
                description: "Failed to submit application",
                variant: "destructive",
            });
        }
    };



    return (
        <div className="flex pt-[8rem]">
            {isClient && (
                <div className="w-64 p-4 border-r hidden md:block fixed h-full">
                    <h2 className="text-lg font-semibold mb-4 flex items-center gap-2">
                        <Briefcase className="h-5 w-5"/>
                        Client Actions
                    </h2>

                    <div className="relative mb-4">
                        <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground"/>
                        <Input
                            placeholder="Search jobs..."
                            className="pl-10"
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                        />
                    </div>

                    <div className="space-y-2">
                        <Dialog>
                            <DialogTrigger asChild>
                                <Button className="w-full justify-start" variant="ghost">
                                    <Plus className="h-4 w-4 mr-2"/>
                                    Add Job Offer
                                </Button>
                            </DialogTrigger>
                            <DialogContent className="sm:max-w-[600px]">
                                <DialogHeader>
                                    <DialogTitle>Create New Job</DialogTitle>
                                    <DialogDescription>
                                        Fill out the form to create a new job listing.
                                    </DialogDescription>
                                </DialogHeader>
                                <JobForm />
                            </DialogContent>
                        </Dialog>

                    </div>
                </div>
            )}

            <Dialog open={!!selectedJobId} onOpenChange={(open) => !open && setSelectedJobId(null)}>
                <DialogContent>
                    <DialogHeader>
                        <DialogTitle>
                            Apply for {selectedJobId && jobs?.find(job => job.jobId === selectedJobId)?.title}
                        </DialogTitle>
                    </DialogHeader>
                    <div className="space-y-4">
                        <div>
                            <h3 className="font-medium">Cover Letter</h3>
                            <textarea
                                className="w-full p-2 border rounded"
                                rows={5}
                                value={applicationCover}
                                onChange={(e) => setApplicationCover(e.target.value)}
                                placeholder="Explain why you're a good fit..."
                            />
                        </div>
                        <Button onClick={handleApply}>
                            {isLoading ? "Applying..." : "Submit"}
                        </Button>
                    </div>
                </DialogContent>
            </Dialog>

            <div className={`${isClient ? 'ml-[16rem]' : ''} flex-1 container py-8 px-4`}>
                <div className="mb-8">
                    <h1 className="text-3xl font-bold mb-6">Job Opportunities</h1>
                    <div className="flex justify-between items-center mb-4">
                        <p className="text-sm text-muted-foreground">
                            {filteredJobs.length} {filteredJobs.length === 1 ? 'job' : 'jobs'} found
                        </p>
                    </div>
                </div>

                {filteredJobs.length === 0 ? (
                    <Alert>
                        <AlertCircle className="h-4 w-4 pt-[8rem]"/>
                        <AlertTitle>No jobs found</AlertTitle>
                        <AlertDescription>
                            {searchTerm ? (
                                `No jobs match "${searchTerm}". Try different keywords.`
                            ) : (
                                "There are currently no available jobs."
                            )}
                        </AlertDescription>
                    </Alert>
                ) : (
                    <div className="grid gap-6">
                        {filteredJobs.map((job: Job) => (
                            <Card
                                key={job.jobId}
                                className="p-6 hover:shadow-md transition-shadow cursor-pointer"
                                onClick={(e) => {
                                    e.stopPropagation();
                                    navigate('/about', {
                                        state: { clientId: job.clientId }
                                    });
                                }}                            >
                                <div className="flex flex-col md:flex-row md:items-start gap-6">
                                    <Avatar className="h-12 w-12 border rounded-md">
                                        <AvatarImage
                                            src={`https://ui-avatars.com/api/?name=${job.clientName}&background=random`}
                                            alt={job.clientName}
                                        />
                                        <AvatarFallback>{job.clientName.charAt(0)}</AvatarFallback>
                                    </Avatar>

                                    <div className="flex-1">
                                        <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-2 mb-2">
                                            <h2 className="text-xl font-semibold">{job.title}</h2>
                                            <div className="flex items-center gap-2">
                                                <Button
                                                    variant="outline"
                                                    size="sm"
                                                    onClick={(e) => {
                                                        e.stopPropagation();
                                                        setSelectedJobId(job.jobId);
                                                    }}
                                                >
                                                    <Bookmark className="h-4 w-4 mr-2"/>
                                                    Apply
                                                </Button>

                                                <Button
                                                    variant="outline"
                                                    size="sm"

                                                >
                                                    <Bookmark className="h-4 w-4 mr-2"/>
                                                    About Company
                                                </Button>
                                            </div>
                                        </div>

                                        <p className="text-sm text-muted-foreground mb-4">{job.clientName}</p>
                                        <p className="mb-4 line-clamp-2">{job.description}</p>

                                        <div className="flex flex-wrap gap-2 mb-4">
                                            {job.requiredSkills.map((skill, i) => (
                                                <span
                                                    key={i}
                                                    className="px-3 py-1 bg-secondary text-secondary-foreground rounded-full text-xs"
                                                >
                                                    {skill}
                                                </span>
                                            ))}
                                        </div>

                                        <div className="flex flex-wrap items-center gap-4 text-sm text-muted-foreground">
                                            <div className="flex items-center gap-1">
                                                <MapPin className="h-4 w-4"/>
                                                <span>{job.location}</span>
                                            </div>
                                            <div className="flex items-center gap-1">
                                                <DollarSign className="h-4 w-4"/>
                                                <span>{job.payment}</span>
                                            </div>
                                            <div className="flex items-center gap-1">
                                                <Clock className="h-4 w-4"/>
                                                <span>Apply by {new Date(job.deadline).toLocaleDateString()}</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </Card>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
};

interface JobFormData {
    title: string;
    description: string;
    location: string;
    payment: string;
    deadline: string;
    requiredSkills: string;
}

const JobForm = () => {
    const [formData, setFormData] = useState<JobFormData>({
        title: '',
        description: '',
        location: '',
        payment: '',
        deadline: '',
        requiredSkills: ''
    });

    const [addJobOffer, { isLoading }] = useAddJobOfferMutation();
    const navigate = useNavigate();
    const location = useLocation();

    const handleSubmit = async (e: React.FormEvent) => {

        e.preventDefault();
        const clientId = location.state?.talent?.id || localStorage.getItem('userId');
        const clientName = localStorage.getItem('companyName') || location.state?.talent?.companyName;

        if (!clientId) {
            navigate('/login');
            return;
        }

        try {

            const deadlineDate = new Date(formData.deadline);

            if (!formData.deadline) {
                alert('Please select a deadline date');
                return;
            }

            const selectedDate = new Date(formData.deadline);
            const today = new Date();
            today.setHours(0, 0, 0, 0);

            if (selectedDate < today) {
                alert('Deadline cannot be in the past');
                return;
            }
            const jobData = {
                clientId,
                clientName,
                title: formData.title,
                description: formData.description,
                requiredSkills: formData.requiredSkills
                    .split(',')
                    .map(skill => skill.trim())
                    .filter(skill => skill.length > 0),
                location: formData.location,
                payment: formData.payment,
                deadline: formData.deadline
            };

            await addJobOffer({ clientId, jobData }).unwrap();
            navigate('/talent-jobs');
        } catch (error) {
            console.error('Failed to create job:', error);
            alert('Failed to create job. Please try again.');
        }
    };


    return (
        <form onSubmit={handleSubmit} className="space-y-4">
            <div className="grid grid-cols-1 gap-4">

                <Input
                    placeholder="Job Title"
                    value={formData.title}
                    onChange={(e) => setFormData({...formData, title: e.target.value})}
                    required
                />
                <Input
                    placeholder="Description"
                    value={formData.description}
                    onChange={(e) => setFormData({...formData, description: e.target.value})}
                    required
                />
                <Input
                    placeholder="Location"
                    value={formData.location}
                    onChange={(e) => setFormData({...formData, location: e.target.value})}
                    required
                />
                <Input
                    placeholder="Payment Amount"
                    value={formData.payment}
                    onChange={(e) => setFormData({...formData, payment: e.target.value})}
                    required
                />
                <Input
                    type="date"
                    placeholder="Deadline (YYYY-MM-DD)"
                    value={formData.deadline}
                    onChange={(e) => setFormData({...formData, deadline: e.target.value})}
                    required
                    min={new Date().toISOString().split('T')[0]}
                />
                <Input
                    placeholder="Required Skills (comma separated)"
                    value={formData.requiredSkills}
                    onChange={(e) => setFormData({...formData, requiredSkills: e.target.value})}
                    required
                />
            </div>
            <Button
                type="submit"
                className="w-full mt-4"
                disabled={isLoading}
            >
                {isLoading ? 'Creating...' : 'Create Job Offer'}
            </Button>
        </form>
    );
};