document.addEventListener('DOMContentLoaded', () => {
    const flashcard = document.querySelector('.flashcard');
    const wordDisplay = document.getElementById('word-display');
    const definitionDisplay = document.getElementById('definition-display');
    const controlsDiv = document.getElementById('controls');
    const messageContainer = document.getElementById('message-container');

    let reviewQueue = [];
    let currentWordIndex = -1;

    async function startReviewSession() {
        messageContainer.innerHTML = '<p>Đang tải phiên ôn tập...</p>';
        try {
            const response = await fetch('/api/review/session');
            reviewQueue = await response.json();

            if (reviewQueue.length === 0) {
                messageContainer.innerHTML = '<h2>Tuyệt vời!</h2><p>Bạn đã hoàn thành tất cả các từ cần ôn tập trong hôm nay.</p>';
                return;
            }

            messageContainer.innerHTML = '';
            currentWordIndex = 0;
            displayCurrentWord();

        } catch (error) {
            console.error("Lỗi khi bắt đầu phiên ôn tập:", error);
            messageContainer.innerHTML = '<p class="error">Không thể tải phiên ôn tập.</p>';
        }
    }

    function displayCurrentWord() {
        if (currentWordIndex >= reviewQueue.length) {
            messageContainer.innerHTML = '<h2>Hoàn thành!</h2><p>Bạn đã ôn xong phiên này. Làm tốt lắm!</p>';
            flashcard.style.display = 'none';
            controlsDiv.style.display = 'none';
            return;
        }

        const wordData = reviewQueue[currentWordIndex];
        flashcard.classList.remove('is-flipped');
        controlsDiv.style.display = 'none'; // Ẩn nút đánh giá khi chưa lật thẻ

        wordDisplay.textContent = wordData.word;
        definitionDisplay.innerHTML = `<strong>${wordData.phonetic || ''}</strong><br>${wordData.definition}`;

        // Thêm sự kiện lật thẻ một lần cho mỗi từ
        flashcard.onclick = () => {
            flashcard.classList.add('is-flipped');
            controlsDiv.style.display = 'block'; // Hiện nút đánh giá sau khi lật
            flashcard.onclick = null; // Gỡ sự kiện để tránh lật lại
        };
    }

    async function handleRatingClick(quality) {
        const wordData = reviewQueue[currentWordIndex];

        try {
            await fetch('/api/review/response', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ wordId: wordData.id, quality: quality })
            });

            // Chuyển sang từ tiếp theo
            currentWordIndex++;
            displayCurrentWord();

        } catch (error) {
            console.error("Lỗi khi gửi đánh giá:", error);
            alert('Không thể lưu kết quả, vui lòng thử lại.');
        }
    }

    document.querySelectorAll('.rating-btn').forEach(button => {
        button.addEventListener('click', () => {
            const quality = parseInt(button.dataset.quality);
            handleRatingClick(quality).then(r => {});
        });
    });

    // Bắt đầu phiên ôn tập khi trang được tải
    startReviewSession().then(r => {});
});