import { useParams, useNavigate, useLocation } from "react-router-dom";
import {useDeleteApplicationMutation, useYourApplicationsQuery} from "@/services/talentApiSlice";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { AlertCircle, Clock, CheckCircle2, XCircle, ChevronLeft } from "lucide-react";
import { Card } from "@/components/ui/card";
import { toast } from "@/hooks/use-toast";

const ApplicationDetails = () => {
    const { applicationId } = useParams();
    const navigate = useNavigate();
    const location = useLocation();
    const talentId = location.state?.talent?.id || localStorage.getItem('userId');

    const { data: applications, isLoading, isError } = useYourApplicationsQuery(talentId);
    const [deleteApplication, { isLoading: isDeleting }] = useDeleteApplicationMutation();

    const application = applications?.find(app => app.applicationId === applicationId);

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

    const handleWithdraw = async () => {
        if (!talentId || !applicationId) return;

        try {
            await deleteApplication({ talentId, applicationId }).unwrap();
            toast({
                title: "Application withdrawn",
                description: "Your application has been successfully withdrawn.",
            });
            navigate(-1);
        } catch (error) {
            toast({
                title: "Error",
                description: "Failed to withdraw application",
                variant: "destructive",
            });
        }
    };

    if (isLoading) {
        return <div className="container mx-auto py-8 mt-[8rem]">Loading...</div>;
    }

    if (isError || !application) {
        return (
            <div className="container mx-auto py-8 pt-[8rem]">
                <Alert variant="destructive">
                    <AlertCircle className="h-4 w-4" />
                    <AlertTitle>Error</AlertTitle>
                    <AlertDescription>
                        Application not found or failed to load.
                    </AlertDescription>
                </Alert>
            </div>
        );
    }

    return (
        <div className="container mx-auto py-8">
            <Button
                variant="ghost"
                onClick={() => navigate(-1)}
                className="mb-6"
            >
                <ChevronLeft className="h-4 w-4 mr-2" />
                Back to Applications
            </Button>

            <div className="max-w-3xl mx-auto">
                <Card className="p-6">
                    <div className="flex justify-between items-start mb-6">
                        <h1 className="text-2xl font-bold">Application Details</h1>
                        {getStatusBadge(application.stats)}
                    </div>

                    <div className="space-y-6">
                        <div>
                            <h2 className="font-medium mb-2">Application ID</h2>
                            <p className="text-muted-foreground">{application.applicationId}</p>
                        </div>

                        <div>
                            <h2 className="font-medium mb-2">Job ID</h2>
                            <p className="text-muted-foreground">{application.jobId}</p>
                        </div>

                        <div>
                            <h2 className="font-medium mb-2">Status</h2>
                            <p className="text-muted-foreground capitalize">{application.stats.toLowerCase()}</p>
                        </div>

                        <div>
                            <h2 className="font-medium mb-2">Cover Letter</h2>
                            <div className="p-4 bg-gray-50 rounded-md">
                                <p className="whitespace-pre-line">{application.coverLetter}</p>
                            </div>
                        </div>

                        {application.stats === 'PENDING' && (
                            <div className="pt-4 border-t">
                                <Button
                                    variant="destructive"
                                    onClick={handleWithdraw}
                                    disabled={isDeleting}
                                >
                                    {isDeleting ? "Withdrawing..." : "Withdraw Application"}
                                </Button>
                                <p className="text-sm text-muted-foreground mt-2">
                                    You can only withdraw applications that are pending.
                                </p>
                            </div>
                        )}
                    </div>
                </Card>
            </div>
        </div>
    );
};

export default ApplicationDetails;