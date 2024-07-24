import FlexBox from '../layout/FlexBox';
import Image from 'next/image';

interface WelcomeProps {
    iconSrc: string;
    description: string;
    className?: string;
    iconSz?: number;
    onClick?: () => void;
}

export default function WelcomeButton({
    iconSrc,
    className,
    onClick,
    description,
    iconSz = 19,
}: WelcomeProps) {
    return (
        <button onClick={onClick}>
            <FlexBox
                direction="row"
                className={`w-[245px] h-[46px] gap-4 justify-center items-center
                    text-base rounded
                    ${className}`}
            >
                <Image
                    src={iconSrc}
                    alt="welcomeicon"
                    width={iconSz}
                    height={iconSz}
                />
                {description}
            </FlexBox>
        </button>
    );
}
