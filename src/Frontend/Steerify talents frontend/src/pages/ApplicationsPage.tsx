import { useYourApplicationsQuery } from "@/services/talentApiSlice";
import { useLocation, useNavigate } from "react-router-dom";
import { Skeleton } from "@/components/ui/skeleton";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { AlertCircle, Clock, CheckCircle2, XCircle, ChevronRight } from "lucide-react";
import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Link } from "react-router-dom";


const ApplicationsPage = () => {

    const location = useLocation();
    const navigate = useNavigate();
    const talentId = location.state?.talent?.id || localStorage.getItem('userId');
    const { data: applications, isLoading, isError, error } = useYourApplicationsQuery(talentId);
const isTalent = location.state?.talent?.role == 'TALENT'
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
                    <Badge variant="destructive" className="flex items-center gap-1 bg-red-300">
                        <XCircle className="h-3 w-3" />
                        Rejected
                    </Badge>
                );
            default:
                return <Badge variant="outline">{status}</Badge>;
        }
    };

    if (isLoading) {
        return (
            <div className="container mx-auto py-8">
                <div className="grid gap-6">
                    {[...Array(3)].map((_, i) => (
                        <Skeleton key={i} className="h-32 w-full rounded-lg" />
                    ))}
                </div>
            </div>
        );
    }

    if (isError) {
        return (
            <div className="container mx-auto py-8 mt-[10rem]">
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
            <div className="container w-[30rem] py-8 mt-[6rem]">
                <Alert>
                    <AlertCircle className="h-4 w-4" />
                    <AlertTitle>No applications found</AlertTitle>
                    <AlertDescription>
                        You haven't applied to any jobs yet.
                    </AlertDescription>
                </Alert>
            </div>
        );
    }

    return (
        <div className="container mx-auto py-8 mt-[6rem]">
            <h1 className="text-3xl font-bold mb-8">Your Job Applications</h1>
            <div className="grid gap-4">
                {applications.map((application) => (
                    <Card key={application.applicationId} className="p-6 hover:shadow-md transition-shadow">
                        <div className="flex flex-col space-y-4">
                            <div className="flex justify-between items-start">
                                <Link
                                    to={`/applications/${application.applicationId}`}
                                    state={{talent: location.state?.talent}}
                                    className="hover:underline"
                                >
                                    <h2 className="text-xl font-semibold">Application
                                        #{application.applicationId.substring(0, 8)}</h2>
                                </Link>
                                {getStatusBadge(application.stats)}
                            </div>

                            <div className="space-y-2">
                                <h3 className="font-medium">Cover Letter</h3>
                                <p className="text-gray-600 whitespace-pre-line">
                                    {application.coverLetter}
                                </p>
                            </div>

                            <div className="flex justify-between items-center pt-2">
      <span className="text-sm text-muted-foreground">
        Applied with Talent ID: {application.talentId.substring(0, 8)}...
      </span>
                                <div className="flex gap-2">
                                    <Button
                                        variant="ghost"
                                        size="sm"
                                        className="gap-1"
                                        onClick={() => navigate(`/jobs/${application.jobId}`)}
                                    >
                                        View Job <ChevronRight className="h-4 w-4"/>
                                    </Button>
                                    <Button
                                        variant="ghost"
                                        size="sm"
                                        className="gap-1"
                                        asChild
                                    >
                                        <Link
                                            to={`/applications/${application.applicationId}`}
                                            state={{talent: location.state?.talent}}
                                        >
                                            Details <ChevronRight className="h-4 w-4"/>
                                        </Link>
                                    </Button>
                                </div>
                            </div>
                        </div>
                    </Card>
                ))}
            </div>
        </div>
    );
};

export default ApplicationsPage;








