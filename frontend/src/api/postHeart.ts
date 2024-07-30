import api from './basic';

interface PostBlackBody {
    nickName: string;
    content: string;
    cleanMethod: number;
}

interface PostHeartResponse {
    postId: {
        timestamp: number;
        date: string;
    };
    userId: string;
    nickName: string;
    content: string;
    cleanMethod: number;
    likeCount: number;
}

async function postBlack(
    body: PostBlackBody
    //userId: string
): Promise<PostHeartResponse> {
    const { data } = await api.post('/black', body, {
        headers: {
            userId: 'test',
        },
    });
    return data;
}

export { postBlack };
export type { PostBlackBody, PostHeartResponse };
