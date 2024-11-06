interface CompleteButtonProps {
    onClick: () => void;
}

export default function CompleteButton({ onClick }: CompleteButtonProps) {
    return (
        <button
            className="bg-[#62467D] w-[120px] h-[42px]
            rounded-3xl text-white text-lg"
            onClick={onClick}
        >
            작성 완료
        </button>
    );
}
