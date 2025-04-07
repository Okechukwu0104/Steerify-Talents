import { useState } from "react";
import {
    useAllCreatedJobsByClientQuery,
    useUpdateApplicationStatusMutation,
    useGetApplicationsByJobIdQuery,
    useDeleteJobMutation,
    useUpdateJobMutation
} from "@/services/talentApiSlice";
import { useLocation, useNavigate } from "react-router-dom";
import { toast } from "@/hooks/use-toast";
import { JobCount } from "@/components/ui/JobCount.tsx";
import {
    Card,
    CardHeader,
    CardTitle,
    CardDescription,
    CardContent,
    CardFooter
} from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import {
    Briefcase,
    Calendar,
    ChevronDown,
    ChevronUp,
    Users,
    Check,
    X
} from "lucide-react";
import { Skeleton } from "@/components/ui/skeleton";
import {
    Dialog,
    DialogContent,
    DialogHeader,
    DialogTitle,
    DialogDescription
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";

interface Application {
    applicationId: string;
    talentId: string;
    jobId: string;
    coverLetter: string;
    stats: 'PENDING' | 'ACCEPTED' | 'REJECTED';
    talent?: {
        firstName: string;
        lastName: string;
        email: string;
        avatar?: string;
    };
}

interface Job {
    jobId: string;
    clientId: string;
    title: string;
    description: string;
    location: string;
    payment: string;
    deadline: string;
    createdAt: string;
    requiredSkills?: string[];
}

const EditJobForm = ({ job, onSuccess }: { job: Job; onSuccess: () => void }) => {
    const [formData, setFormData] = useState({
        title: job.title,
        description: job.description,
        location: job.location,
        payment: job.payment,
        deadline: new Date(job.deadline).toISOString().split('T')[0],
        requiredSkills: job.requiredSkills?.join(', ') || ''
    });

    const [updateJob, { isLoading }] = useUpdateJobMutation();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            await updateJob({
                jobId: job.jobId,
                jobData: {
                    title: formData.title,
                    description: formData.description,
                    location: formData.location,
                    payment: formData.payment,
                    deadline: formData.deadline,
                    requiredSkills: formData.requiredSkills
                        .split(',')
                        .map(skill => skill.trim())
                        .filter(skill => skill.length > 0),
                    jobId: "",
                    clientId: "",
                    clientName: ""
                }
            }).unwrap();

            toast({
                title: "Success",
                description: "Job updated successfully",
            });
            onSuccess();
        } catch (err) {
            toast({
                title: "Error",
                description: "Failed to update job",
                variant: "destructive",
            });
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
                    placeholder="Deadline"
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
                {isLoading ? 'Updating...' : 'Update Job Offer'}
            </Button>
        </form>
    );
};

const JobOffers = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const client = location.state?.client;
    const {
        data: jobs = [],
        isLoading,
        isError,
        refetch: refetchJobs
    } = useAllCreatedJobsByClientQuery(client?.id);

    const [expandedJobId, setExpandedJobId] = useState<string | null>(null);
    const [applicationCounts, setApplicationCounts] = useState<Record<string, number>>({});
    const [updateStatus] = useUpdateApplicationStatusMutation();
    const [deleteJob] = useDeleteJobMutation();
    const [editingJob, setEditingJob] = useState<Job | null>(null);

    const {
        data: applications = [],
        isLoading: isLoadingApplications,
    } = useGetApplicationsByJobIdQuery(expandedJobId || '', {
        skip: !expandedJobId,
    });

    const toggleJobExpansion = (jobId: string) => {
        setExpandedJobId(prev => prev === jobId ? null : jobId);
    };

    const handleStatusUpdate = async (applicationId: string, status: 'ACCEPTED' | 'REJECTED') => {
        try {
            await updateStatus({
                applicationId,
                status
            }).unwrap();
            toast({
                title: "Success",
                description: `Application ${status.toLowerCase()}`,
            });
            refetchJobs();
            if (expandedJobId) {
                setExpandedJobId(null);
                setTimeout(() => setExpandedJobId(expandedJobId), 100);
            }
        } catch (err) {
            toast({
                title: "Error",
                description: "Failed to update status",
                variant: "destructive",
            });
        }
    };

    const handleDeleteJob = async (jobId: string) => {
        try {
            await deleteJob(jobId).unwrap();
            toast({
                title: "Success",
                description: "Job deleted successfully",
            });
            refetchJobs();
        } catch (err) {
            toast({
                title: "Error",
                description: "Failed to delete job",
                variant: "destructive",
            });
        }
    };

    if (isLoading) {
        return (
            <div className="container mx-auto py-8 space-y-4">
                {[...Array(3)].map((_, i) => (
                    <Skeleton key={i} className="h-32 w-full rounded-lg" />
                ))}
            </div>
        );
    }

    if (isError) {
        return (
            <div className="container mx-auto py-8 pt-[5rem]">
                <div className="text-center py-12">
                    <h3 className="text-lg font-medium">Failed to load job applications</h3>
                    <p className="text-muted-foreground text-sm mt-2">
                        Please try again later
                    </p>
                    <Button className="mt-4" onClick={() => window.location.reload()}>
                        Retry
                    </Button>
                </div>
            </div>
        );
    }

    if (!jobs?.length) {
        return (
            <div className="container mx-auto py-8">
                <div className="text-center py-12">
                    <Briefcase className="mx-auto h-12 w-12 text-muted-foreground" />
                    <h3 className="mt-4 text-lg font-medium">No jobs posted yet</h3>
                    <p className="text-muted-foreground text-sm mt-2">
                        Create your first job posting to start receiving applications
                    </p>
                    <Button className="mt-4" onClick={() => navigate('/create-job')}>
                        Create Job Posting
                    </Button>
                </div>
            </div>
        );
    }

    return (
        <div className="container mx-auto py-8 pt-[9rem]">
            <Dialog open={!!editingJob} onOpenChange={(open) => !open && setEditingJob(null)}>
                <DialogContent className="sm:max-w-[600px]">
                    <DialogHeader>
                        <DialogTitle>Edit Job Offer</DialogTitle>
                        <DialogDescription>
                            Make changes to your job listing here.
                        </DialogDescription>
                    </DialogHeader>
                    {editingJob && (
                        <EditJobForm
                            job={editingJob}
                            onSuccess={() => {
                                setEditingJob(null);
                                refetchJobs();
                            }}
                        />
                    )}
                </DialogContent>
            </Dialog>

            <div className="mb-8">
                <h1 className="text-2xl font-bold">Your Job Postings</h1>
                <p className="text-muted-foreground">
                    {jobs.length} {jobs.length === 1 ? 'job' : 'jobs'} posted
                </p>
            </div>

            <div className="space-y-4">
                {jobs.map((job: Job) => {
                    const showApplications = expandedJobId === job.jobId;
                    const count = applicationCounts[job.jobId] || 0;

                    return (
                        <Card key={job.jobId} className="overflow-hidden">
                            <button
                                className="w-full text-left"
                                onClick={() => toggleJobExpansion(job.jobId)}
                                aria-expanded={showApplications}
                            >
                                <CardHeader className="hover:bg-muted/50 transition-colors">
                                    <div className="flex items-center justify-between">
                                        <div className="flex items-center gap-4">
                                            <Briefcase className="h-5 w-5 text-primary" />
                                            <div>
                                                <CardTitle>{job.title}</CardTitle>
                                                <CardDescription className="flex items-center gap-2 mt-1">
                                                    <Calendar className="h-4 w-4" />
                                                    • Deadline: {new Date(job.deadline).toLocaleDateString()}
                                                </CardDescription>
                                            </div>
                                        </div>
                                        <div className="flex items-center gap-4">
                                            <Badge variant="outline" className="flex items-center gap-1">
                                                <Users className="h-4 w-4" />
                                                <JobCount jobId={job.jobId} />
                                            </Badge>
                                            {showApplications ? (
                                                <ChevronUp className="h-5 w-5 text-muted-foreground" />
                                            ) : (
                                                <ChevronDown className="h-5 w-5 text-muted-foreground" />
                                            )}
                                        </div>
                                    </div>
                                </CardHeader>
                            </button>

                            {showApplications && (
                                <CardContent className="border-t pt-4">
                                    <div className="mt-6 space-y-4">
                                        {isLoadingApplications ? (
                                            <div className="space-y-4">
                                                {[...Array(3)].map((_, i) => (
                                                    <Skeleton key={i} className="h-20 w-full rounded-lg"/>
                                                ))}
                                            </div>
                                        ) : applications.length === 0 ? (
                                            <p className="text-muted-foreground text-sm">No applications yet</p>
                                        ) : (
                                            applications.map((application: Application) => (
                                                <Card key={application.applicationId} className="p-4">
                                                    <div className="flex items-start gap-4">
                                                        <Avatar>
                                                            <AvatarImage src={application.talent?.avatar}/>
                                                            <AvatarFallback>
                                                                {application.talent?.firstName?.[0]}{application.talent?.lastName?.[0]}
                                                            </AvatarFallback>
                                                        </Avatar>
                                                        <div className="flex-1">
                                                            <div className="flex items-center justify-between">
                                                                <h4 className="font-medium">
                                                                    {application.talent?.firstName} {application.talent?.lastName}
                                                                </h4>
                                                                {application.stats === 'ACCEPTED' ? (
                                                                    <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">
                                                                        Accepted
                                                                    </span>
                                                                ) : application.stats === 'REJECTED' ? (
                                                                    <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-red-100 text-red-800">
                                                                        Rejected
                                                                    </span>
                                                                ) : (
                                                                    <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-gray-100 text-gray-800">
                                                                        Pending
                                                                    </span>
                                                                )}
                                                            </div>
                                                            <p className="text-sm text-muted-foreground mt-1">
                                                                {application.talent?.email}
                                                            </p>
                                                            <p className="mt-2 text-sm">{application.coverLetter}</p>
                                                            <div className="flex gap-2 mt-3">
                                                                <Button
                                                                    size="sm"
                                                                    onClick={() => handleStatusUpdate(application.applicationId, 'ACCEPTED')}
                                                                    className={
                                                                        application.stats === 'ACCEPTED'
                                                                            ? "bg-green-600 hover:bg-green-700 text-white"
                                                                            : "bg-green-100 hover:bg-green-200 text-green-800 border border-green-300"
                                                                    }
                                                                >
                                                                    <Check className="h-4 w-4 mr-2"/>
                                                                    {application.stats === 'ACCEPTED' ? 'Accepted' : 'Accept'}
                                                                </Button>
                                                                <Button
                                                                    size="sm"
                                                                    onClick={() => handleStatusUpdate(application.applicationId, 'REJECTED')}
                                                                    className={
                                                                        application.stats === 'REJECTED'
                                                                            ? "bg-red-600 hover:bg-red-700 text-white"
                                                                            : "bg-red-100 hover:bg-red-200 text-red-800 border border-red-300"
                                                                    }
                                                                >
                                                                    <X className="h-4 w-4 mr-2"/>
                                                                    {application.stats === 'REJECTED' ? 'Rejected' : 'Reject'}
                                                                </Button>
                                                                <Button
                                                                    className='backdrop-blur bg-accent-purple outline-neutral-600 hover:bg-fuchsia-900'
                                                                    onClick={() => navigate('/profile-page', {state: {talent: application.talentId}}) }
                                                                >
                                                                    Talent Details
                                                                </Button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </Card>
                                            ))
                                        )}
                                    </div>
                                </CardContent>
                            )}

                            <CardFooter className="border-t p-4">
                                <div className="flex justify-between w-full">
                                    <Button
                                        variant="ghost"
                                        onClick={() => {
                                            const jobToEdit = jobs.find(j => j.jobId === job.jobId);
                                            if (jobToEdit) {
                                                setEditingJob(jobToEdit);
                                            }
                                        }}
                                    >
                                        Edit Job Offer
                                    </Button>
                                    <Button
                                        variant="ghost"
                                        onClick={() => handleDeleteJob(job.jobId)}
                                        className="text-red-600 hover:text-red-800"
                                    >
                                        Delete Job Offer
                                    </Button>
                                </div>
                            </CardFooter>
                        </Card>
                    );
                })}
            </div>
        </div>
    );
};

export default JobOffers;