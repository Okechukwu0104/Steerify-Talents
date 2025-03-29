import { User, UserRole } from '@/types/user';
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar.tsx";

interface UserProfileProps {
    user: User;
    showRole?: boolean;
}

const UserProfile = ({ user, showRole = true }: UserProfileProps) => {
    return (
        <div className="max-w-md mx-auto bg-white rounded-xl shadow-md overflow-hidden md:max-w-2xl">
            <div className="p-8">
                <div className="flex items-center gap-6 mb-6">
                    <Avatar className="h-24 w-24">
                        <AvatarImage src={user.avatarUrl} />
                        <AvatarFallback className="text-3xl font-medium">
                            {user.firstName.charAt(0)}{user.lastName.charAt(0)}
                        </AvatarFallback>
                    </Avatar>
                    <div>
                        <h1 className="text-2xl font-bold text-gray-800">
                            {user.firstName} {user.lastName}
                        </h1>
                        <p className="text-gray-600">{user.email}</p>
                        {showRole && user.role && (
                            <span className="inline-block mt-2 px-3 py-1 text-xs font-semibold rounded-full bg-blue-100 text-blue-800">
                {user.role}
              </span>
                        )}
                    </div>
                </div>

                <div className="border-t pt-6">
                    <h2 className="text-lg font-semibold text-gray-700 mb-4">Profile Information</h2>
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div>
                            <p className="text-sm text-gray-500">First Name</p>
                            <p className="font-medium">{user.firstName}</p>
                        </div>
                        <div>
                            <p className="text-sm text-gray-500">Last Name</p>
                            <p className="font-medium">{user.lastName}</p>
                        </div>
                        <div>
                            <p className="text-sm text-gray-500">Email</p>
                            <p className="font-medium">{user.email}</p>
                        </div>
                        <div>
                            <p className="text-sm text-gray-500">Role</p>
                            <p className="font-medium">{user.role || '-'}</p>
                        </div>
                    </div>
                </div>

                {user.bio && (
                    <div className="mt-6 border-t pt-6">
                        <h2 className="text-lg font-semibold text-gray-700 mb-2">About</h2>
                        <p className="text-gray-600">{user.bio}</p>
                    </div>
                )}
            </div>
        </div>
    );
};

export default UserProfile;