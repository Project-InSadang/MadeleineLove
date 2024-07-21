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
            <FlexBox direction="col" className="relative z-10 w-screen h-screen gap-8">
                <div className="flex mt-12 w-full pl-7">
                    <HomeButton />
                </div>
                <div className="">
                    닉네임을 적어주세요 (선택)
                    <TextBox />
                </div>
                <div className="">
                    최고의 사랑을 풀어주세요
                    <TextBox />
                </div>
                <div className="">
                    어떻게 비워낼까요?
                    <div className="grid grid-cols-2 gap-4">
                        <MethodButton description="폭파" heartSrc={heartimg} />
                        <MethodButton description="폭파" heartSrc={heartimg} />
                        <MethodButton description="폭파" heartSrc={heartimg} />
                        <MethodButton description="폭파" heartSrc={heartimg} />
                    </div>
                </div>
                <CompleteButton />{' '}
            </FlexBox>
        </>
    );
}
