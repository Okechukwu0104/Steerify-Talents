import {Badge, Users} from "lucide-react";
import {useGetApplicationsByJobIdQuery} from "@/services/talentApiSlice.ts";


export const JobCount = ({ jobId }: { jobId: string }) => {
    const { data, isLoading, isError } = useGetApplicationsByJobIdQuery(jobId);

    if (isLoading) return (
        <Badge className="flex items-center gap-1">
            ...
        </Badge>
    );

    if (isError) return (
        <Badge  className="flex items-center gap-1">
            Error
        </Badge>
    );

    return (
        <p  className="flex items-center gap-1">

            {data?.length || 0} {data?.length === 1 ? 'applicant' : 'applicants'}
        </p>
    );
};