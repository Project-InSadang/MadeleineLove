import api from './basic';

interface PostBlackBody {
    nickName: string;
    content: string;
    cleanMethod: number;
}

interface PostWhiteBody {
    nickName: string;
    content: string;
    fillMethod: number;
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

async function postWhite(
    body: PostWhiteBody
    //userId: string
): Promise<void> {
    const { data } = await api.post('/white', body, {
        headers: {
            userId: 'test',
        },
    });
    return data;
}

export { postBlack, postWhite };
export type { PostBlackBody, PostWhiteBody, PostHeartResponse };
