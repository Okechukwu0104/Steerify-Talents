import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { useGetTalentDetailsQuery } from "@/services/talentApiSlice";
import { useLocation } from "react-router-dom";
import { Skeleton } from "@/components/ui/skeleton";

export const TalentSettingsPage = () => {
    const location = useLocation();
    const talentId = location.state?.talent;
    const { data: talent, isLoading, isError } = useGetTalentDetailsQuery(talentId);
    console.log(talent)
    if (isLoading) {
        return (
            <div className="max-w-md mx-auto bg-white rounded-xl shadow-md overflow-hidden md:max-w-2xl p-8">
                <div className="flex items-center gap-6 mb-6">
                    <Skeleton className="h-24 w-24 rounded-full" />
                    <div className="space-y-2">
                        <Skeleton className="h-6 w-48" />
                        <Skeleton className="h-4 w-32" />
                    </div>
                </div>
                <div className="space-y-4">
                    {[...Array(4)].map((_, i) => (
                        <div key={i} className="space-y-2">
                            <Skeleton className="h-4 w-20" />
                            <Skeleton className="h-5 w-full" />
                        </div>
                    ))}
                </div>
            </div>
        );
    }

    if (isError || !talent) {
        return (
            <div className="max-w-md mx-auto bg-white rounded-xl shadow-md overflow-hidden md:max-w-2xl p-8 text-center mt-[7rem]">
                <p className="text-red-500">Failed to load talent profile</p>
            </div>
        );
    }

    return (
        <div className="max-w-md mx-auto bg-white rounded-xl shadow-md overflow-hidden md:max-w-2xl">
            <div className="p-8">
                <div className="flex items-center gap-6 mb-6">
                    <Avatar className="h-24 w-24">
                        <AvatarImage src={talent} />
                        <AvatarFallback className="text-3xl font-medium  bg-accent-green">
                            {talent.firstName?.charAt(0)}{talent.lastName?.charAt(0)}
                        </AvatarFallback>
                    </Avatar>
                    <div>
                        <h1 className="text-2xl font-bold text-gray-800">
                            {talent.firstName} {talent.lastName}
                        </h1>
                        <p className="text-gray-600">{talent.email}</p>
                        {talent.role && (
                            <span className="inline-block mt-2 px-3 py-1 text-xs font-semibold rounded-full bg-blue-100 text-blue-800">
                {talent.role}
              </span>
                        )}
                    </div>
                </div>

                <div className="border-t pt-6">
                    <h2 className="text-lg font-semibold text-gray-700 mb-4">Profile Information</h2>
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div>
                            <p className="text-sm text-gray-500">First Name</p>
                            <p className="font-medium">{talent.firstName || '-'}</p>
                        </div>
                        <div>
                            <p className="text-sm text-gray-500">Last Name</p>
                            <p className="font-medium">{talent.lastName || '-'}</p>
                        </div>
                        <div>
                            <p className="text-sm text-gray-500">Email</p>
                            <p className="font-medium">{talent.email || '-'}</p>
                        </div>
                        <div>
                            <p className="text-sm text-gray-500">Phone</p>
                            <p className="font-medium">{talent.phoneNumber || '-'}</p>
                        </div>
                        <div>
                            <p className="text-sm text-gray-500">Age</p>
                            <p className="font-medium">{talent.age || '-'}</p>
                        </div>
                        <div>
                            <p className="text-sm text-gray-500">Gender</p>
                            <p className="font-medium">{talent.gender || '-'}</p>
                        </div>
                        <div className="md:col-span-2">
                            <p className="text-sm text-gray-500">Address</p>
                            <p className="font-medium">{talent.address || '-'}</p>
                        </div>
                        <div className="md:col-span-2">
                            <p className="text-sm text-gray-500">Education</p>
                            <p className="font-medium">{talent.education || '-'}</p>
                        </div>
                    </div>
                </div>

                {talent.skills?.length > 0 && (
                    <div className="mt-6 border-t pt-6">
                        <h2 className="text-lg font-semibold text-gray-700 mb-2">Skills</h2>
                        <div className="flex flex-wrap gap-2">
                            {talent.skills.map((skill, index) => (
                                <span
                                    key={index}
                                    className="px-3 py-1 text-xs font-semibold rounded-full bg-gray-100 text-gray-800"
                                >
                  {skill}
                </span>
                            ))}
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

