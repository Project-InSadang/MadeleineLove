import Image from 'next/image';
import FlexBox from '../layout/FlexBox';
import homeimg from '@/public/icon/main_home.svg';
import leftimg from '@/public/icon/chevron_left.svg';

interface HomeButtonProps {
    onClick: () => void;
}

export default function HomeButton({ onClick }: HomeButtonProps) {
    return (
        <button onClick={onClick}>
            <FlexBox direction="row" className="gap-2 justify-center">
                <Image src={leftimg} alt="Home Icon" width={10} height={10} />
                <Image src={homeimg} alt="Home Icon" width={28} height={28} />
            </FlexBox>
        </button>
    );
}
