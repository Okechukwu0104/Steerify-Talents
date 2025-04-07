import { useGetTalentPostsQuery } from "@/services/talentApiSlice.ts";
import { Card } from "@/components/ui/card.tsx";
import { useLocation, useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { Skeleton } from "@/components/ui/skeleton";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { AlertCircle, Search, PlusCircle, List, MessageSquare, Send } from "lucide-react";
import { Avatar, AvatarFallback, AvatarImage } from "@radix-ui/react-avatar";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { JSXElementConstructor, Key, ReactElement, ReactNode, useState} from "react";

const PostsPage = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const {data: posts, isLoading, isError} = useGetTalentPostsQuery();
    const [searchQuery, setSearchQuery] = useState("");
    const [searchQuery2, setSearchQuery2] = useState("");
    const [activeCommentPost, setActiveCommentPost] = useState<string | null>(null);
    const [comments, setComments] = useState<Record<string, Array<{
        text: string;
        author: string;
        date: string;
    }>>>({});
    const [commentText, setCommentText] = useState("");
    let index = 0;


    const talent = location.state?.talent || {
        firstName: "Guest",
        lastName: "User",
        email: "guest@example.com",
    };
    const isTalent = talent.role === "talent" || localStorage.getItem("userRole") === "talent";

    // @ts-ignore
    const filteredPosts = posts?.filter(post =>
        post.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
        post.description.includes(searchQuery.toLowerCase()) ||
        post.tags?.some(tag => tag.toLowerCase().includes(searchQuery.toLowerCase()))
    ) || [];

    const handleAddPost = () => {
        navigate("/create-post");
    };

    const toggleComments = (postId: string) => {
        setActiveCommentPost(activeCommentPost === postId ? null : postId);
    };

    const handleAddComment = (postId: string) => {
        if (!commentText.trim()) return;

        const newComment = {
            text: commentText,
            author: `${talent.firstName} ${talent.lastName}`,
            date: new Date().toLocaleString()
        };

        setComments(prev => ({
            ...prev,
            [postId]: [...(prev[postId] || []), newComment]
        }));

        setCommentText("");
    };

    if (isLoading) {
        return (
            <div className="space-y-4 p-16 pt-32">
                <h1 className="text-2xl font-bold mb-6">Recent Posts</h1>
                {[...Array(3)].map((_, i) => (
                    <Card key={i} className="p-6">
                        <div className="space-y-3">
                            <Skeleton className="h-6 w-3/4"/>
                            <Skeleton className="h-4 w-full"/>
                            <Skeleton className="h-4 w-5/6"/>
                            <div className="flex gap-2 pt-2">
                                <Skeleton className="h-4 w-20"/>
                                <Skeleton className="h-4 w-20"/>
                            </div>
                        </div>
                    </Card>
                ))}
            </div>
        );
    }

    if (isError) {
        return (
            <div className="p-16 pt-32">
                <Alert variant="destructive">
                    <AlertCircle className="h-4 w-4"/>
                    <AlertTitle>Error loading posts</AlertTitle>
                </Alert>
            </div>
        );
    }

    // @ts-ignore
    return (
        <div className="min-h-screen flex flex-col">
            <div className="flex h-screen">
                <div className="w-64 bg-white shadow-sm border-r flex flex-col align-middle mt-32 ">
                    <div className="p-4 border-b">
                        <Button
                            variant="ghost"
                            className="font-semibold text-lg w-full justify-start"
                            onClick={() => navigate('/dashboard')}
                        >
                            Steerify {talent.role} Connect
                        </Button>
                    </div>

                    <div className="p-4 space-y-4">
                        <div className="relative">
                            <Search
                                className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400"/>
                            <Input
                                type="text"
                                placeholder="Search posts..."
                                className="pl-10"
                                value={searchQuery}
                                onChange={(e) => setSearchQuery(e.target.value)}
                            />
                        </div>

                        <div className="relative">
                            <Search
                                className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400"/>
                            <Input
                                type="text"
                                placeholder="Search by tags..."
                                className="pl-10"
                                value={searchQuery2}
                                onChange={(e) => setSearchQuery2(e.target.value)}
                            />
                        </div>
                    </div>

                    <div className="flex-1 p-2 space-y-1">
                        {isTalent &&(
                            <Button
                                variant="ghost"
                                className="w-full justify-start gap-2"
                                onClick={handleAddPost}
                            >
                                <PlusCircle className="h-4 w-4"/>
                                Create Post
                            </Button>
                        )}

                        {!isTalent &&(
                            <Button
                                variant="ghost"
                                className="w-full justify-start gap-2"
                                onClick={handleAddPost}
                            >
                                <PlusCircle className="h-4 w-4"/>
                                Create Post
                            </Button>
                        )}


                        <Button
                            variant="ghost"
                            className="w-full justify-start gap-2"
                            onClick={() => navigate('/my-posts')}
                        >
                            <List className="h-4 w-4"/>
                            My Posts
                        </Button>

                    </div>

                    <div className="p-4 border-t flex items-center gap-3">
                        <Avatar className="h-8 w-8">
                            <AvatarImage src={talent.avatar}/>
                            <AvatarFallback>
                                {talent.firstName.charAt(0)}{talent.lastName.charAt(0)}
                            </AvatarFallback>
                        </Avatar>
                        <div>
                            <p className="text-sm font-medium">{talent.firstName} {talent.lastName}</p>
                            <p className="text-xs text-gray-500">{talent.email}</p>
                        </div>
                    </div>
                </div>

                <div className="flex-1 overflow-auto">
                    <main className="flex-grow p-16 pt-32">
                        <div className="max-w-7xl mx-auto">
                            <div className="flex justify-between items-center mb-8">
                                <h1 className="text-2xl font-bold">Recent Posts</h1>
                                <div className="text-sm text-muted-foreground">
                                    {filteredPosts.length} posts found
                                </div>
                            </div>

                            {filteredPosts.length === 0 ? (
                                <div className="flex flex-col items-center justify-center h-64">
                                    <div className="text-center space-y-2">
                                        <h3 className="text-lg font-medium">No posts found</h3>
                                        <p className="text-sm text-muted-foreground">
                                            {searchQuery ?
                                                "No posts match your search. Try different keywords." :
                                                "There are currently no posts available."
                                            }
                                        </p>
                                    </div>
                                </div>
                            ) : (
                                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                                    {filteredPosts.map((post) => (
                                        <Card key={post.postId} className="hover:shadow-md transition-shadow">
                                            <div className="p-6">
                                                <div className="flex justify-between items-start mb-4">
                                                    <h2 className="text-lg font-semibold line-clamp-2">{post.title}</h2>
                                                    <span
                                                        className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-primary/10 text-primary">
                                                    {post.role || "TALENT"}
                                                </span>
                                                </div>

                                                <p className="text-sm text-muted-foreground mb-4 line-clamp-3">
                                                    {post.description}
                                                </p>

                                                <div className="space-y-2 mb-4">
                                                    <p className="text-sm">
                                                <span
                                                    className="font-medium">Price:</span> {post.price || 'Not specified'}
                                                    </p>
                                                    <p className="text-sm">
                                                <span
                                                    className="font-medium">Contact:</span> {post.phoneNumber || 'Not provided'}
                                                    </p>
                                                </div>

                                                <div className="flex items-center justify-between text-sm">
                                                    <div className="flex items-center space-x-2">
                                                        <Avatar className="h-6 w-6">
                                                            <AvatarImage src={post.author?.avatar}/>
                                                            <AvatarFallback>
                                                                {post?.name?.charAt(0) + post?.name?.charAt(1) || 'A'}
                                                            </AvatarFallback>
                                                        </Avatar>
                                                        <span>{post?.name || 'Anonymous'}</span>
                                                    </div>

                                                </div>

                                                {post.tags && post.tags.length > 0 && (
                                                    <div className="flex flex-wrap gap-2 mt-4">
                                                        {post.tags.map((tag: boolean | ReactElement<any, string | JSXElementConstructor<any>> | Iterable<ReactNode> | Key) => (
                                                            <span
                                                                key={tag}
                                                                className="inline-flex items-center px-2 py-1 rounded-md text-xs font-medium bg-secondary text-secondary-foreground"
                                                            >
                                                        {tag}
                                                    </span>
                                                        ))}
                                                    </div>
                                                )}

                                                <div className="mt-6 border-t pt-4">
                                                    <Button
                                                        variant="ghost"
                                                        className="flex items-center gap-2 text-sm w-full justify-start"
                                                        onClick={() => toggleComments(post.postId)}
                                                    >
                                                        <MessageSquare className="h-4 w-4"/>
                                                        {comments[post.postId]?.length || 0} comments
                                                    </Button>

                                                    {activeCommentPost === post.postId && (
                                                        <div className="mt-4 space-y-4">
                                                            <div className="space-y-3 max-h-60 overflow-y-auto">
                                                                {comments[post.postId]?.map((comment, index) => (
                                                                    <div key={index} className="flex gap-3">
                                                                        <Avatar className="h-8 w-8">
                                                                            <AvatarFallback>
                                                                                {comment.author.charAt(0)}
                                                                            </AvatarFallback>
                                                                        </Avatar>
                                                                        <div className="flex-1">
                                                                            <div className="bg-gray-100 rounded-lg p-3">
                                                                                <p className="font-medium text-sm">{comment.author}</p>
                                                                                <p className="text-sm">{comment.text}</p>
                                                                                <p className="text-xs text-gray-500 mt-1">{comment.date}</p>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                ))}

                                                                {(!comments[post.postId] || comments[post.postId].length === 0) && (
                                                                    <p className="text-sm text-gray-500 text-center py-4">
                                                                        No comments yet. Be the first to comment!
                                                                    </p>
                                                                )}
                                                            </div>

                                                            <div className="flex gap-2">
                                                                <Input
                                                                    placeholder="Add a comment..."
                                                                    value={commentText}
                                                                    onChange={(e) => setCommentText(e.target.value)}
                                                                    onKeyDown={(e) => {
                                                                        if (e.key === 'Enter' && commentText.trim()) {
                                                                            handleAddComment(post.postId);
                                                                        }
                                                                    }}
                                                                />
                                                                <Button
                                                                    size="icon"
                                                                    onClick={() => handleAddComment(post.postId)}
                                                                    disabled={!commentText.trim()}
                                                                >
                                                                    <Send className="h-4 w-4"/>
                                                                </Button>
                                                            </div>
                                                        </div>
                                                    )}
                                                </div>
                                            </div>
                                        </Card>
                                    ))}
                                </div>
                            )}
                        </div>
                    </main>
                </div>
            </div>



        </div>
    );
};

export default PostsPage;