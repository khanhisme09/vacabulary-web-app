document.addEventListener('DOMContentLoaded', () => {
    const flashcard = document.querySelector('.flashcard');
    const wordDisplay = document.getElementById('word-display');
    const definitionDisplay = document.getElementById('definition-display');
    const flipBtn = document.getElementById('flip-btn');
    const nextBtn = document.getElementById('next-btn');
    const messageContainer = document.getElementById('message-container');

    async function fetchNextWord() {
        flashcard.classList.remove('is-flipped');
        wordDisplay.textContent = 'Đang tải...';
        definitionDisplay.textContent = '';
        try {
            const response = await fetch('/api/review/next-word');
            if (!response.ok) {
                wordDisplay.textContent = 'Hết từ để ôn';
                messageContainer.innerHTML = '<p>Bạn đã ôn hết từ vựng đã lưu. Hãy học thêm từ mới nhé!</p>';
                flipBtn.disabled = true;
                return;
            }
            const data = await response.json();
            wordDisplay.textContent = data.word;
            definitionDisplay.innerHTML = `<strong>${data.phonetic || ''}</strong><br>${data.definition}`;
            flipBtn.disabled = false;
        } catch (error) {
            console.error('Lỗi:', error);
            wordDisplay.textContent = 'Lỗi';
        }
    }

    flipBtn.addEventListener('click', () => {
        flashcard.classList.toggle('is-flipped');
    });

    nextBtn.addEventListener('click', fetchNextWord);

    // Tải từ đầu tiên khi trang được mở
    fetchNextWord();
});