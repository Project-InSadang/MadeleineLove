import TwoCircle from '@/components/background/TwoCircle';
import FlexBox from '@/components/layout/FlexBox';
import HomeButton from '@/components/button/HomeButton';
import TextBox from '@/components/Box/TextBox';
import MethodButton from '@/components/button/MethodButton';
import heartimg from '@/public/icon/heart/17 a.svg';
import CompleteButton from '@/components/button/CompleteButton';

export default function Black() {
    return (
        <>
            <div className="flex absolute z-0 w-screen h-screen">
                <TwoCircle />
            </div>
            <div className="relative z-10 w-screen h-screen">
                <div className="flex py-9 w-full pl-7">
                    <HomeButton />
                </div>
                <FlexBox direction="col" className="px-10 gap-7 pb-7">
                    <FlexBox direction="col" className="items-start w-full gap-2.5 text-lg">
                        닉네임을 적어주세요 (선택)
                        <TextBox />
                    </FlexBox>
                    <FlexBox direction="col" className="items-start w-full gap-2.5 text-lg">
                        최고의 사랑을 풀어주세요
                        <TextBox />
                    </FlexBox>
                    <FlexBox direction="col" className="w-full gap-2.5 text-lg items-start">
                        어떻게 비워낼까요?
                        <div className="grid grid-cols-2 gap-4 w-full">
                            <MethodButton description="닦아서" heartSrc={heartimg} />
                            <MethodButton description="날려서" heartSrc={heartimg} />
                            <MethodButton description="태워서" heartSrc={heartimg} />
                            <MethodButton description="먹어서" heartSrc={heartimg} />
                        </div>
                    </FlexBox>
                    <CompleteButton />
                </FlexBox>
            </div>
        </>
    );
}
