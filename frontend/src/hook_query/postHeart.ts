import { useMutation } from '@tanstack/react-query';
import {
    postBlack,
    postWhite,
    PostBlackBody,
    PostWhiteBody,
} from '../api/postHeart';

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

function usePostWhite() {
    //userId 관리
    const { mutate } = useMutation({
        mutationKey: ['postWhite'],
        mutationFn: (body: PostWhiteBody) =>
            postWhite(
                body
                //userId
            ),
    });
    return { mutate };
}

export { usePostBlack, usePostWhite };
