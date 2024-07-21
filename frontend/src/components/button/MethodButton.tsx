import Image from 'next/image';
import FlexBox from '../layout/FlexBox';

interface MethodProps {
    heartSrc: string;
    description: string;
}

export default function MethodButton({ heartSrc, description }: MethodProps) {
    return (
        <FlexBox
            direction="row"
            className="h-[65px] px-5 gap-3 text-black
            shadow-[-5px_-5px_15px_#62467d_inset,_-20px_-20px_15px_rgba(0,_0,_0,_0.15)_inset,_20px_20px_15px_rgba(255,_255,_255,_0.8)_inset] bg-white rounded-xl"
        >
            <Image src={heartSrc} alt={'heart'} width={40} height={40} />
            <div>{description}</div>
        </FlexBox>
    );
}
