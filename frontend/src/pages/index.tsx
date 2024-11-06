import OneCircle from '@/components/background/OneCircle';
import FlexBox from '@/components/layout/FlexBox';
import letterheart from '@/public/icon/heart/letter_heart.svg';
import Image from 'next/image';
import logo from '@/public/icon/logo.svg';
import WelcomeButton from '@/components/button/WelcomeButton';
import naver from '@/public/icon/naver.svg';
import talkheart from '@/public/icon/heart/talk_heart.svg';

export default function Welcome() {
    return (
        <>
            <div className="flex absolute z-0 w-screen h-screen">
                <OneCircle />
            </div>
            <div className="relative z-10 w-screen h-screen">
                <FlexBox
                    direction="col"
                    className="justify-center w-full h-full gap-12"
                >
                    <Image
                        src={letterheart}
                        alt="welcomeheart"
                        width={160}
                        height={160}
                    />
                    <Image src={logo} alt="logotypo" width={270} height={270} />
                    <FlexBox direction="col" className="gap-4 mt-5">
                        <WelcomeButton
                            iconSrc={naver}
                            description={'네이버 로그인'}
                            className="bg-white text-[#767678]"
                        />
                        <WelcomeButton
                            iconSrc={talkheart}
                            description={'하트 구경하기'}
                            className="bg-[#866BDE] text-white"
                            iconSz={28}
                        />
                    </FlexBox>
                </FlexBox>
            </div>
        </>
    );
}

//logo 화질 개선
// 버튼 onClick
