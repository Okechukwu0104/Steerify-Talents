import { useGetJobAppPerOfferQuery } from "@/services/talentApiSlice";
import { useNavigate, useParams } from "react-router-dom";
import { Skeleton } from "@/components/ui/skeleton";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { AlertCircle, Clock, CheckCircle2, XCircle, ChevronRight, Mail, Calendar, User, Check, X } from "lucide-react";
import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar.tsx";

const ClientApplicationsPage = () => {
    const { jobId } = useParams();
    const navigate = useNavigate();
    const { data: applications, isLoading, isError, error } = useGetJobAppPerOfferQuery(jobId!);

    const getStatusBadge = (status: string) => {
        switch (status) {
            case 'PENDING':
                return (
                    <Badge variant="secondary" className="flex items-center gap-1">
                        <Clock className="h-3 w-3" />
                        Pending
                    </Badge>
                );
            case 'ACCEPTED':
                return (
                    <Badge className="flex items-center gap-1 bg-green-100 text-green-800">
                        <CheckCircle2 className="h-3 w-3" />
                        Accepted
                    </Badge>
                );
            case 'REJECTED':
                return (
                    <Badge variant="destructive" className="flex items-center gap-1">
                        <XCircle className="h-3 w-3" />
                        Rejected
                    </Badge>
                );
            default:
                return <Badge variant="outline">{status}</Badge>;
        }
    };

    const handleStatusUpdate = (applicationId: string, status: string) => {
        console.log(`Updating application ${applicationId} to ${status}`);
    };

    if (isLoading) {
        return (
            <div className="container mx-auto py-8">
                <div className="grid gap-6">
                    {[...Array(3)].map((_, count) => (
                        <Skeleton key={count} className="h-32 w-full rounded-lg" />
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
                        {error?.toString() || 'Failed to load applications. Please try again later.'}
                    </AlertDescription>
                </Alert>
            </div>
        );
    }

    if (!applications || applications.length === 0) {
        return (
            <div className="container mx-auto py-8">
                <Alert>
                    <AlertCircle className="h-4 w-4" />
                    <AlertTitle>No applications found</AlertTitle>
                    <AlertDescription>
                        No applications received yet for this job.
                    </AlertDescription>
                </Alert>
            </div>
        );
    }

    return (
        <div className="container mx-auto py-8">
            <h1 className="text-3xl font-bold mb-8">Job Applications</h1>
            <div className="space-y-4">
                {applications.map((application) => (
                    <Card key={application.applicationId} className="p-6 hover:shadow-md transition-shadow">
                        <div className="flex items-start gap-4">
                            <Avatar className="h-10 w-10 border">
                                <AvatarFallback>
                                    {application.talentId.substring(0, 2).toUpperCase()}
                                </AvatarFallback>
                            </Avatar>

                            <div className="flex-1 space-y-3">
                                <div className="flex justify-between items-start">
                                    <div>
                                        <h2 className="text-xl font-semibold">
                                            Applicant {application.talentId.substring(0, 8)}
                                        </h2>
                                    </div>
                                    {getStatusBadge(application.stats)}
                                </div>

                                <div className="space-y-2">
                                    <h3 className="font-medium">Cover Letter</h3>
                                    <p className="text-gray-600 whitespace-pre-line">
                                        {application.coverLetter}
                                    </p>
                                </div>

                                <div className="flex items-center gap-4 text-sm text-muted-foreground">
                                    <div className="flex items-center gap-1">
                                        <Calendar className="h-4 w-4" />
                                        <span>
                                            Applied with Talent ID: {application.talentId.substring(0, 8)}...
                                        </span>
                                    </div>
                                </div>
                            </div>

                            <div className="flex flex-col gap-2">
                                <Button
                                    variant="outline"
                                    size="sm"
                                    onClick={() => navigate(`/talent-profile/${application.talentId}`)}
                                >
                                    <User className="h-4 w-4 mr-2" />
                                    View Profile
                                </Button>
                                {application.stats === 'PENDING' && (
                                    <>
                                        <Button
                                            size="sm"
                                            onClick={() => handleStatusUpdate(application.applicationId, 'ACCEPTED')}
                                        >
                                            <Check className="h-4 w-4 mr-2" />
                                            Accept
                                        </Button>
                                        <Button
                                            variant="destructive"
                                            size="sm"
                                            onClick={() => handleStatusUpdate(application.applicationId, 'REJECTED')}
                                        >
                                            <X className="h-4 w-4 mr-2" />
                                            Reject
                                        </Button>
                                    </>
                                )}
                            </div>
                        </div>
                    </Card>
                ))}
            </div>
        </div>
    );
};

export default ClientApplicationsPage;