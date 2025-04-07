import { useLocation, useNavigate } from "react-router-dom";
import { useGetClientDetailsQuery } from "@/services/talentApiSlice";
import { Button } from "@/components/ui/button";

const AboutPage = () => {
    const location = useLocation();
    const clientId = location.state?.clientId;
    const { data: clientDetails } = useGetClientDetailsQuery(clientId);
    const navigate = useNavigate();

    return (

            <div className="container mx-auto py-8 pt-[8rem]">
                <Button
                    variant="outline"
                    onClick={() => navigate(-1)}
                    className="mb-6"
                >
                    Back to Jobs
                </Button>
                <h1 className="text-3xl font-bold mb-6">About Company</h1>

                {clientDetails ? (
                    <div className="space-y-4 max-w-2xl">
                        <div>
                            <h2 className="text-2xl font-semibold">{clientDetails.companyName}</h2>
                        </div>

                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div>
                                <h3 className="font-medium">Contact Person</h3>
                                <p>{clientDetails.contactPerson || `${clientDetails.firstName} ${clientDetails.lastName}`}</p>
                            </div>

                            <div>
                                <h3 className="font-medium">Email</h3>
                                <p>{clientDetails.email}</p>
                            </div>
                        </div>

                        <div>
                            <h3 className="font-medium">Phone</h3>
                            <p>{clientDetails.phone}</p>
                        </div>

                        {clientDetails.description && (
                            <div>
                                <h3 className="font-medium">About</h3>
                                <p className="text-muted-foreground">{clientDetails.description}</p>
                            </div>
                        )}
                    </div>
                ) : (
                    <p>No company information available</p>
                )}
            </div>

    );
};

export default AboutPage;