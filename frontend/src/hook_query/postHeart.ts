import { useMutation } from '@tanstack/react-query';
import { postBlack, PostBlackBody } from '../api/postHeart';

function usePostBlack() {
    //userId 관리
    const { mutate } = useMutation({
        mutationKey: ['postBlack'],
        mutationFn: (body: PostBlackBody) =>
            postBlack(
                body
                //userId
            ),
    });
    return { mutate };
}

export { usePostBlack };
