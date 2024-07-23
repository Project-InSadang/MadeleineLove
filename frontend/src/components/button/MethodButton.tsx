import Image from 'next/image';
import FlexBox from '../layout/FlexBox';

interface MethodProps {
    heartSrc: string;
    description: string;
    isSelected: boolean;
    onClick: () => void;
}

export default function MethodButton({ heartSrc, description, isSelected, onClick }: MethodProps) {
    return (
        <button onClick={onClick}>
            <FlexBox
                direction="row"
                className={`text-black text-lg items-center justify-center
            h-[65px] w-[143px] gap-3
            shadow-[-3px_-3px_15px_#62467d_inset,-5px_-5px_7px_rgba(0,_0,_0,_0.15)_inset]
            ${isSelected ? 'bg-[#D2CDE9]' : 'bg-white'} rounded-xl
            transition-colors duration-300`}
            >
                <Image src={heartSrc} alt={'heart'} width={40} height={40} />
                {description}
            </FlexBox>
        </button>
    );
}
