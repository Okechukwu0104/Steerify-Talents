// import { useGetTalentJobsQuery } from "@/services/talentApiSlice.ts";
// import { Card } from "@/components/ui/card.tsx";
// import { useLocation, useNavigate } from "react-router-dom";
// import { Skeleton } from "@/components/ui/skeleton";
// import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
// import { AlertCircle, Search, Bookmark, Briefcase, MapPin, DollarSign, Clock } from "lucide-react";
// import { Avatar, AvatarFallback, AvatarImage } from "@radix-ui/react-avatar";
// import { Input } from "@/components/ui/input";
// import { Button } from "@/components/ui/button";
// import { useState } from "react";
//
// export const JobsPage = () => {
//     const location = useLocation();
//     const navigate = useNavigate();
//     const data = useGetTalentJobsQuery();
//
// console.log(data);
//
// };
import { useGetTalentJobsQuery } from "@/services/talentApiSlice";
import { Card } from "@/components/ui/card";
import { useNavigate } from "react-router-dom";
import { Skeleton } from "@/components/ui/skeleton";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { AlertCircle, Search, Bookmark, MapPin, DollarSign, Clock } from "lucide-react";
import { Avatar, AvatarFallback, AvatarImage } from "@radix-ui/react-avatar";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { useState } from "react";

export const JobsPage = () => {
    const navigate = useNavigate();
    const { data: jobs, isLoading, isError, error } = useGetTalentJobsQuery();
    const [searchTerm, setSearchTerm] = useState("");
    const [locationFilter, setLocationFilter] = useState("");
    const [skillFilter, setSkillFilter] = useState("");

    const filteredJobs = jobs?.filter(job => {
        const matchesSearch = job.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
            job.description.toLowerCase().includes(searchTerm.toLowerCase());
        const matchesLocation = locationFilter ? job.location.toLowerCase().includes(locationFilter.toLowerCase()) : true;
        const matchesSkill = skillFilter ? job.requiredSkills.some(skill =>
            skill.toLowerCase().includes(skillFilter.toLowerCase())) : true;

        return matchesSearch && matchesLocation && matchesSkill;
    }) || [];

    if (isLoading) {
        return (
            <div className="container mx-auto py-8">
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
            <div className="container mx-auto py-8">
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

    return (
        <div className="container mx-auto py-8">
            <div className="mb-8">
                <h1 className="text-3xl font-bold mb-6">Available Job Opportunities</h1>

                <div className="grid md:grid-cols-3 gap-4 mb-6">
                    <div className="relative">
                        <Search className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
                        <Input
                            placeholder="Search jobs..."
                            className="pl-10"
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                        />
                    </div>
                    <Input
                        placeholder="Filter by location"
                        value={locationFilter}
                        onChange={(e) => setLocationFilter(e.target.value)}
                    />
                    <Input
                        placeholder="Filter by skills"
                        value={skillFilter}
                        onChange={(e) => setSkillFilter(e.target.value)}
                    />
                </div>

                <div className="flex justify-between items-center mb-4">
                    <p className="text-sm text-muted-foreground">
                        {filteredJobs.length} {filteredJobs.length === 1 ? 'job' : 'jobs'} found
                    </p>
                </div>
            </div>

            {filteredJobs.length === 0 ? (
                <Alert>
                    <AlertCircle className="h-4 w-4" />
                    <AlertTitle>No jobs found</AlertTitle>
                    <AlertDescription>
                        Try adjusting your search or filter criteria.
                    </AlertDescription>
                </Alert>
            ) : (
                <div className="grid gap-6">
                    {filteredJobs.map((job) => (
                        <Card
                            key={job.jobId}
                            className="p-6 hover:shadow-md transition-shadow cursor-pointer"
                            onClick={() => navigate(`/jobs/${job.jobId}`)}
                        >
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
                                            <Button variant="outline" size="sm">
                                                <Bookmark className="h-4 w-4 mr-2" />
                                                Save
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
                                            <MapPin className="h-4 w-4" />
                                            <span>{job.location}</span>
                                        </div>
                                        <div className="flex items-center gap-1">
                                            <DollarSign className="h-4 w-4" />
                                            <span>{job.payment}</span>
                                        </div>
                                        <div className="flex items-center gap-1">
                                            <Clock className="h-4 w-4" />
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
    );
};