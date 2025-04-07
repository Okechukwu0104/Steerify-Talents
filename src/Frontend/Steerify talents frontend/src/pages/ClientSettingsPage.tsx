import { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { logout } from '@/store/authSlice';
import { Button } from '@/components/ui/button';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import { User, Briefcase, Settings, Trash2, Edit, Save, X, LogOut } from 'lucide-react';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { toast } from '@/hooks/use-toast';
import {
    useDeleteTalentMutation,
    useUpdateTalentMutation,
    useDeleteClientMutation,
    useUpdateClientMutation
} from '@/services/talentApiSlice';
import {
    Dialog,
    DialogContent,
    DialogHeader,
    DialogTitle,
    DialogDescription,
    DialogFooter,
    DialogTrigger
} from '@/components/ui/dialog';

const SettingsPage = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const dispatch = useDispatch();

    const user = location.state?.talent || location.state?.client;
    const isTalent = user?.role === 'TALENT';

    const [isEditing, setIsEditing] = useState(false);
    const [formData, setFormData] = useState({
        firstName: user?.firstName || '',
        lastName: user?.lastName || '',
        email: user?.email || '',
        phone: user?.phone || '',
        address: user?.address || '',
        ...(isTalent ? {
            skills: user?.skills?.join(', ') || '',
            education: user?.education || ''
        } : {
            companyName: user?.companyName || '',
            industry: user?.industry || ''
        })
    });

    const [updateTalent] = useUpdateTalentMutation();
    const [deleteTalent] = useDeleteTalentMutation();
    const [updateClient] = useUpdateClientMutation();
    const [deleteClient] = useDeleteClientMutation();

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSave = async () => {
        try {
            const payload = {
                firstName: formData.firstName,
                lastName: formData.lastName,
                email: formData.email,
                phone: formData.phone,
                address: formData.address,
                ...(isTalent ? {
                    skills: formData.skills.split(',').map(skill => skill.trim()),
                    education: formData.education
                } : {
                    companyName: formData.companyName,
                    industry: formData.industry
                })
            };

            if (isTalent) {
                await updateTalent({
                    talentId: user.id,
                    talentDto: payload
                }).unwrap();
            } else {
                await updateClient({
                    clientId: user.id,
                    clientDto: payload
                }).unwrap();
            }

            toast({ title: "Success", description: "Profile updated successfully" });
            setIsEditing(false);
        } catch (err) {
            toast({ title: "Error", description: "Failed to update profile", variant: "destructive" });
        }
    };

    const handleDeleteAccount = async () => {
        try {
            if (isTalent) {
                await deleteTalent(user.id).unwrap();
            } else {
                await deleteClient(user.id).unwrap();
            }

            dispatch(logout());
            localStorage.removeItem('token');
            localStorage.removeItem('userRole');
            localStorage.removeItem('userId');

            toast({ title: "Success", description: "Account deleted successfully" });
            navigate('/');
        } catch (err) {
            toast({ title: "Error", description: "Failed to delete account", variant: "destructive" });
        }
    };

    const handleLogout = () => {
        dispatch(logout());
        localStorage.removeItem('token');
        localStorage.removeItem('userRole');
        localStorage.removeItem('userId');
        navigate('/login');
    };

    if (!user) {
        return <div className="flex justify-center mt-60">No user data found</div>;
    }

    return (
        <div className="container mx-auto py-8 pt-[8rem]">
            <div className="flex flex-col md:flex-row gap-8">

                <div className="w-full lg:w-1/3">
                    <div className="bg-white rounded-lg shadow-sm border p-6">
                        <div className="flex flex-col items-center mb-6">
                            <Avatar className="h-24 w-24 mb-4">
                                <AvatarImage src={user?.avatarUrl} />
                                <AvatarFallback>
                                    {user?.firstName?.charAt(0)}{user?.lastName?.charAt(0)}
                                </AvatarFallback>
                            </Avatar>

                            {!isEditing ? (
                                <>
                                    <h2 className="text-xl font-bold">
                                        {user.firstName} {user.lastName}
                                    </h2>
                                    <p className="text-gray-600">{user.email}</p>
                                    {!isTalent && <p className="text-sm text-gray-500 mt-1">{user.companyName}</p>}
                                </>
                            ) : (
                                <div className="w-full space-y-3">
                                    <div className="grid grid-cols-2 gap-3">
                                        <Input
                                            name="firstName"
                                            value={formData.firstName}
                                            onChange={handleInputChange}
                                            placeholder="First Name"
                                        />
                                        <Input
                                            name="lastName"
                                            value={formData.lastName}
                                            onChange={handleInputChange}
                                            placeholder="Last Name"
                                        />
                                    </div>
                                    <Input
                                        name="email"
                                        type="email"
                                        value={formData.email}
                                        onChange={handleInputChange}
                                        placeholder="Email"
                                    />
                                </div>
                            )}
                        </div>

                        <div className="flex justify-center gap-2">
                            {!isEditing ? (
                                <>
                                    <Button onClick={() => setIsEditing(true)}>
                                        <Edit className="h-4 w-4 mr-2" /> Edit Profile
                                    </Button>
                                    <Dialog>
                                        <DialogTrigger asChild>
                                            <Button variant="destructive">
                                                <Trash2 className="h-4 w-4 mr-2" /> Delete Account
                                            </Button>
                                        </DialogTrigger>
                                        <DialogContent>
                                            <DialogHeader>
                                                <DialogTitle>Delete Account</DialogTitle>
                                                <DialogDescription>
                                                    Are you sure? This action cannot be undone.
                                                </DialogDescription>
                                            </DialogHeader>
                                            <DialogFooter>
                                                <Button variant="outline">Cancel</Button>
                                                <Button variant="destructive" onClick={handleDeleteAccount}>
                                                    Delete
                                                </Button>
                                            </DialogFooter>
                                        </DialogContent>
                                    </Dialog>
                                </>
                            ) : (
                                <>
                                    <Button onClick={handleSave}>
                                        <Save className="h-4 w-4 mr-2" /> Save Changes
                                    </Button>
                                    <Button variant="outline" onClick={() => setIsEditing(false)}>
                                        <X className="h-4 w-4 mr-2" /> Cancel
                                    </Button>
                                </>
                            )}
                        </div>
                    </div>
                </div>

                <div className="w-full md:w-2/3">
                    <div className="bg-white rounded-lg shadow-sm border p-6">
                        <h2 className="text-xl font-bold mb-6">Profile Information</h2>

                        <div className="mb-6">
                            <h3 className="font-semibold mb-4 flex items-center gap-2">
                                <User className="h-5 w-5" /> Personal Information
                            </h3>
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                {!isEditing ? (
                                    <>
                                        <div>
                                            <Label className="text-gray-500">First Name</Label>
                                            <p>{user.firstName}</p>
                                        </div>
                                        <div>
                                            <Label className="text-gray-500">Last Name</Label>
                                            <p>{user.lastName}</p>
                                        </div>
                                        <div>
                                            <Label className="text-gray-500">Email</Label>
                                            <p>{user.email}</p>
                                        </div>
                                        <div>
                                            <Label className="text-gray-500">Phone</Label>
                                            <p>{user.phone || '-'}</p>
                                        </div>
                                        <div className="md:col-span-2">
                                            <Label className="text-gray-500">Address</Label>
                                            <p>{user.address || '-'}</p>
                                        </div>
                                    </>
                                ) : (
                                    <>
                                        <div>
                                            <Label>Phone</Label>
                                            <Input
                                                name="phone"
                                                value={formData.phone}
                                                onChange={handleInputChange}
                                            />
                                        </div>
                                        <div className="md:col-span-2">
                                            <Label>Address</Label>
                                            <Input
                                                name="address"
                                                value={formData.address}
                                                onChange={handleInputChange}
                                            />
                                        </div>
                                    </>
                                )}
                            </div>
                        </div>

                        <div className="mb-6">
                            <h3 className="font-semibold mb-4 flex items-center gap-2">
                                <Briefcase className="h-5 w-5" />
                                {isTalent ? 'Professional Information' : 'Company Information'}
                            </h3>
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                {isTalent ? (
                                    !isEditing ? (
                                        <>
                                            <div>
                                                <Label className="text-gray-500">Skills</Label>
                                                <p>{user.skills?.join(', ') || '-'}</p>
                                            </div>
                                            <div>
                                                <Label className="text-gray-500">Education</Label>
                                                <p>{user.education || '-'}</p>
                                            </div>
                                        </>
                                    ) : (
                                        <>
                                            <div className="md:col-span-2">
                                                <Label>Skills (comma separated)</Label>
                                                <Input
                                                    name="skills"
                                                    value={formData.skills}
                                                    onChange={handleInputChange}
                                                />
                                            </div>
                                            <div className="md:col-span-2">
                                                <Label>Education</Label>
                                                <Input
                                                    name="education"
                                                    value={formData.education}
                                                    onChange={handleInputChange}
                                                />
                                            </div>
                                        </>
                                    )
                                ) : (
                                    !isEditing ? (
                                        <>
                                            <div>
                                                <Label className="text-gray-500">Company Name</Label>
                                                <p>{user.companyName || '-'}</p>
                                            </div>
                                            <div>
                                                <Label className="text-gray-500">Industry</Label>
                                                <p>{user.industry || '-'}</p>
                                            </div>
                                        </>
                                    ) : (
                                        <>
                                            <div>
                                                <Label>Company Name</Label>
                                                <Input
                                                    name="companyName"
                                                    value={formData.companyName}
                                                    onChange={handleInputChange}
                                                />
                                            </div>
                                            <div>
                                                <Label>Industry</Label>
                                                <Input
                                                    name="industry"
                                                    value={formData.industry}
                                                    onChange={handleInputChange}
                                                />
                                            </div>
                                        </>
                                    )
                                )}
                            </div>
                        </div>

                        {/* Account Actions */}
                        <div className="border-t pt-6">
                            <h3 className="font-semibold mb-4 flex items-center gap-2">
                                <Settings className="h-5 w-5" /> Account Actions
                            </h3>
                            <div className="space-y-3">
                                <Button variant="outline" className="w-full" onClick={handleLogout}>
                                    <LogOut className="h-4 w-4 mr-2" /> Logout
                                </Button>
                                <Dialog>
                                    <DialogTrigger asChild>
                                        <Button variant="destructive" className="w-full">
                                            <Trash2 className="h-4 w-4 mr-2" /> Delete Account
                                        </Button>
                                    </DialogTrigger>
                                    <DialogContent>
                                        <DialogHeader>
                                            <DialogTitle>Delete Account</DialogTitle>
                                            <DialogDescription>
                                                This will permanently delete your account and all associated data.
                                            </DialogDescription>
                                        </DialogHeader>
                                        <DialogFooter>
                                            <Button variant="outline">Cancel</Button>
                                            <Button variant="destructive" onClick={handleDeleteAccount}>
                                                Delete Account
                                            </Button>
                                        </DialogFooter>
                                    </DialogContent>
                                </Dialog>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default SettingsPage;