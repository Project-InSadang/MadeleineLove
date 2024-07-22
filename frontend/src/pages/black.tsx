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
                    <div className="pb-2">
                        <div className="text-lg mb-2.5">어떻게 비워낼까요?</div>
                        <div className="grid grid-cols-2 gap-3">
                            <MethodButton description="닦아서" heartSrc={bottleheart} />
                            <MethodButton description="날려서" heartSrc={ballonheart} />
                            <MethodButton description="태워서" heartSrc={fireheart} />
                            <MethodButton description="먹어서" heartSrc={cakeheart} />
                        </div>
                    </div>
                    <CompleteButton />
                </FlexBox>
            </div>
        </>
    );
}
