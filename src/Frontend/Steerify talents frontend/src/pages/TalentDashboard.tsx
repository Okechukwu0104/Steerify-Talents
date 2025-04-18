import { useLocation, useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { logout } from "@/store/authSlice";
import { Button } from "@/components/ui/button";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import {
    LayoutDashboard,
    Briefcase,
    User,
    Mail,
    Settings,
    LogOut, Bookmark,
} from "lucide-react";
import {talentApiSlice, useYourApplicationsQuery, useGetJobsQuery} from "@/services/talentApiSlice.ts";
import React from "react";

const TalentDashboard = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const talent = location.state?.talent || {
        firstName: "Guest",
        lastName: "User",
        email: "guest@example.com",
    };
    const isTalent = localStorage.getItem('userRole') === 'talent';

    console.log(talent)
    const handleLogout = () => {
        // Clear Redux state
        dispatch(logout());

        dispatch(talentApiSlice.util.resetApiState());

        localStorage.removeItem('token');
        localStorage.removeItem('user');

        navigate('/login', {replace: true});
    };
    return (
        <div className="flex h-screen bg-gray-50 pt-[8rem]">
            <div className="w-64 bg-white border-r border-gray-200 flex flex-col">
                <div className="p-5 border-b border-gray-200">
                    <h1 className="text-xl font-semibold">{talent.role.charAt(0).toUpperCase() + talent.role.slice(1).toLowerCase()} Portal </h1>
                </div>
                <div className="flex-2 pt-16 pl-3">
                    <div className="flex items-center gap-3 mb-3">
                        <Avatar>
                            <AvatarImage src=""/>
                            <AvatarFallback>
                                {talent.firstName.charAt(0)}{talent.lastName.charAt(0)}
                            </AvatarFallback>
                        </Avatar>
                        <div>
                            <p className="font-medium text-sm">{talent.firstName} {talent.lastName}</p>
                            <p className="text-[11px] text-gray-500">{talent.email}</p>
                        </div>
                    </div>

                    <nav className="space-y-1">
                        <Button
                            variant="ghost"
                            className="w-full justify-start gap-2"
                            onClick={() => navigate("/dashboard")}
                        >
                            <LayoutDashboard className="h-4 w-4"/>
                            Dashboard
                        </Button>
                        <Button
                            variant="ghost"
                            className="w-full justify-start gap-2"
                            onClick={() => navigate("/talent-jobs", {state: {talent: talent}})}
                        >
                            <Briefcase className="h-4 w-4"/>
                            Job Opportunities
                        </Button>


                        <Button
                            variant="ghost"
                            className="w-full justify-start gap-2"
                            onClick={() => navigate("/posts", {state: {talent: talent}})}
                        >
                            <User className="h-4 w-4"/>
                            Recent Posts
                        </Button>

                        {isTalent &&(
                            <Button
                                variant="ghost"
                                className="w-full justify-start gap-2"
                                onClick={() => navigate("/applications", {state: {talent: talent}})}
                            >
                                <Mail className="h-4 w-4"/>
                                Your Job Applications
                            </Button>
                        )}

                        {!isTalent &&(
                            <Button
                                className="w-full justify-start"
                                variant="ghost"
                                onClick={() => navigate('/my-offers', {state: {client: talent}})}
                            >
                                <Bookmark className="h-4 w-4 mr-2"/>
                                My Job Offers
                            </Button>
                        )}


                        <Button
                            variant="ghost"
                            className="w-full justify-start gap-2"
                            onClick={() => navigate("/settings",{state: {talent: talent}})}
                        >
                            <Settings className="h-4 w-4"/>
                            Settings
                        </Button>
                    </nav>
                </div>

                <div className="p-4 border-t border-gray-200">
                    <Button
                        variant="ghost"
                        className="w-full justify-start gap-2 text-red-500 hover:text-red-600"
                        onClick={handleLogout}
                    >
                        <LogOut className="h-4 w-4"/>
                        Logout
                    </Button>
                </div>
            </div>

            <div className="flex-1 overflow-auto p-8 align-bottom">
                <div className="max-w-4xl mx-auto">

                    <div className="mb-8">
                        <h1 className="text-3xl font-bold mb-2">
                            Hello {talent.firstName} {talent.lastName}
                        </h1>
                        <p className="text-lg text-gray-600">
                            Another day... another aim at success
                        </p>
                    </div>


                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">

                        <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-100">
                            <h3 className="font-medium mb-4">Your Profile Strength</h3>
                            <div className="h-2 bg-gray-200 rounded-full mb-2">
                                <div className="h-2 bg-blue-500 rounded-full w-3/4"></div>
                            </div>
                            <p className="text-sm text-gray-500">75% complete</p>
                        </div>
                        {isTalent && (
                            <>
                                <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-100">
                                    <h3 className="font-medium mb-4">Recent Applications</h3>
                                    <p className="text-gray-500 mb-2">Frontend Developer at TechCorp</p>
                                    <p className="text-gray-500">UI Designer at Creative Studio</p>
                                </div>

                                <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-100">
                                    <h3 className="font-medium mb-4">Job Offers for You</h3>
                                    <p className="text-gray-500 mb-2">Senior React Developer</p>
                                    <p className="text-gray-500">UX/UI Designer</p>
                                </div>
                            </>
                        )}

                        {!isTalent && (
                        <>
                            <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-100">
                                <h3 className="font-medium mb-4">Recent offers</h3>
                                <p className="text-gray-500 mb-2">Frontend Developer at TechCorp</p>
                                <p className="text-gray-500">UI Designer at Creative Studio</p>
                            </div>

                            <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-100">
                                <h3 className="font-medium mb-4">Recommended Candidates</h3>
                                <p className="text-gray-500 mb-2">Senior React Developer</p>
                                <p className="text-gray-500">UX/UI Designer</p>
                            </div>
                        </>


                            )}
                    </div>
                </div>
            </div>
        </div>
    );

};
// @ts-ignore
export default TalentDashboard;