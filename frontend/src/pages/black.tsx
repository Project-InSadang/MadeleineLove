import TwoCircle from '@/components/background/TwoCircle';
import FlexBox from '@/components/layout/FlexBox';
import HomeButton from '@/components/button/HomeButton';
import TextBox from '@/components/Box/TextBox';
import MethodButton from '@/components/button/MethodButton';
import CompleteButton from '@/components/button/CompleteButton';
import ballonheart from '@/public/icon/heart/ballon_heart.svg';
import fireheart from '@/public/icon/heart/fire_heart.svg';
import cakeheart from '@/public/icon/heart/cake_heart.svg';
import bottleheart from '@/public/icon/heart/bottle_heart.svg';
import Modal from '@/components/Box/Modal';
import { useState, useEffect } from 'react';
import { usePostBlack } from '@/hook_query/postHeart';

export default function Black() {
    const [selectedMethod, setSelectedMethod] = useState<number | null>(null);
    const [loveMessage, setLoveMessage] = useState<string>('');
    const [nickname, setNickname] = useState<string>('레니');
    const [showModal, setShowModal] = useState<boolean>(false);
    const [modalContent, setModalContent] = useState<string>('');
    const [modalButtonCount, setModalButtonCount] = useState<'one' | 'two'>(
        'one'
    );
    const { mutate: postBlack } = usePostBlack();

    useEffect(() => {}, [nickname, loveMessage, selectedMethod]);

    const handleEmptyLove = () => {
        if (loveMessage !== '') {
            setModalContent('작성을 취소하시겠습니까?');
            setModalButtonCount('two');
            setShowModal(true);
        } else {
            // 이동
        }
    };

    const handleComplete = () => {
        if (loveMessage === '') {
            setModalContent('글을 작성해주세요');
            setModalButtonCount('one');
        } else if (selectedMethod === null) {
            setModalContent('비우기 방법을 선택해주세요');
            setModalButtonCount('one');
        } else {
            setModalContent('작성을 완료하시겠습니까?');
            setModalButtonCount('two');
        }
        setShowModal(true);
    };

    const handleCancel = () => {
        setShowModal(false);
    };

    const handleMethodClick = (methodId: number) => {
        setSelectedMethod(methodId);
    };

    const handleConfirm = () => {
        setShowModal(false);
        postBlack({
            nickName: nickname,
            content: loveMessage,
            cleanMethod: selectedMethod || 0, // 0이면 null , 백엔드와 상의
        });
        console.log('api성공');
        // 이동
    };

    return (
        <>
            <div className="flex absolute z-0 w-screen h-screen">
                <TwoCircle />
            </div>
            <div className="relative z-10 w-screen h-screen">
                <div className="flex py-9 w-full pl-7">
                    <HomeButton onClick={handleEmptyLove} />
                </div>
                <FlexBox direction="col" className="px-10 gap-7 pb-7">
                    <div>
                        <div className="text-lg mb-2.5">
                            닉네임을 적어주세요 (선택){' '}
                        </div>
                        <TextBox
                            height={40}
                            className="rounded-3xl px-4 py-2"
                            placeholder="레니"
                            onChange={(e) => setNickname(e.target.value)}
                            maxLength={12}
                        />
                    </div>
                    <div>
                        <div className="text-lg mb-2.5">
                            최악의 사랑을 풀어주세요{' '}
                        </div>
                        <TextBox
                            height={270}
                            className="rounded-2xl p-4"
                            onChange={(e) => setLoveMessage(e.target.value)}
                            maxLength={500}
                        />
                    </div>
                    <div className="pb-1.5">
                        <div className="text-lg mb-2.5">어떻게 비워낼까요?</div>
                        <div className="grid grid-cols-2 gap-3">
                            <MethodButton
                                description="닦아서"
                                heartSrc={bottleheart}
                                isSelected={selectedMethod === 1}
                                onClick={() => handleMethodClick(1)}
                            />
                            <MethodButton
                                description="날려서"
                                heartSrc={ballonheart}
                                isSelected={selectedMethod === 2}
                                onClick={() => handleMethodClick(2)}
                            />
                            <MethodButton
                                description="태워서"
                                heartSrc={fireheart}
                                isSelected={selectedMethod === 3}
                                onClick={() => handleMethodClick(3)}
                            />
                            <MethodButton
                                description="먹어서"
                                heartSrc={cakeheart}
                                isSelected={selectedMethod === 4}
                                onClick={() => handleMethodClick(4)}
                            />
                        </div>
                    </div>
                    <CompleteButton onClick={handleComplete} />
                </FlexBox>
            </div>
            {showModal && (
                <div className="absolute z-20">
                    <Modal
                        description={modalContent}
                        buttonCount={modalButtonCount}
                        onConfirm={handleConfirm}
                        onCancle={handleCancel}
                    />
                </div>
            )}
        </>
    );
}
