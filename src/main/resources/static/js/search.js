// File: src/main/resources/static/js/search.js

document.addEventListener('DOMContentLoaded', () => {
    const searchForm = document.getElementById('search-form');
    const searchInput = document.getElementById('search-input');
    const resultsDiv = document.getElementById('search-results');

    // ... hàm xử lý searchForm submit giữ nguyên ...
    searchForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        const searchTerm = searchInput.value.trim();
        if (!searchTerm) return;
        resultsDiv.innerHTML = '<p>Đang tìm kiếm...</p>';
        try {
            const response = await fetch(`/api/words/search?q=${encodeURIComponent(searchTerm)}`);
            if (!response.ok) {
                if (response.status === 404) {
                    resultsDiv.innerHTML = `<p class="error">Không tìm thấy từ "${searchTerm}"</p>`;
                } else { throw new Error('Lỗi từ server!'); }
                return;
            }
            const data = await response.json();
            displayWord(data);
        } catch (error) {
            console.error('Có lỗi xảy ra:', error);
            resultsDiv.innerHTML = '<p class="error">Không thể thực hiện tìm kiếm. Vui lòng thử lại.</p>';
        }
    });

    function displayWord(wordData) {
        // Lấy thông tin xác thực từ thẻ meta hoặc một element trên trang
        const isAuthenticated = document.querySelector('div[sec\\:authorize="isAuthenticated"]') != null;

        let saveButtonHtml = '';
        // Chỉ hiển thị nút lưu nếu người dùng đã đăng nhập
        if (isAuthenticated) {
            saveButtonHtml = `<button class="save-word-btn" data-word-id="${wordData.id}">Lưu từ này</button>`;
        }

        resultsDiv.innerHTML = `
            <div class="word">${wordData.word} ${saveButtonHtml}</div>
            <div class="phonetic">${wordData.phonetic || ''}</div>
            <div class="definition"><strong>Định nghĩa:</strong> ${wordData.definition}</div>
            <div class="example"><strong>Ví dụ:</strong> <em>"${wordData.example}"</em></div>
            <div id="save-status"></div>
        `;
    }



    // Dùng event delegation để xử lý click trên nút được tạo động
    resultsDiv.addEventListener('click', async function(event) {
        if (event.target && event.target.classList.contains('save-word-btn')) {
            const button = event.target;
            const wordId = button.getAttribute('data-word-id');
            const saveStatusDiv = document.getElementById('save-status');

            try {
                // API này được bảo vệ bởi Spring Security.
                // Trình duyệt sẽ tự động gửi cookie session, vì vậy chúng ta không cần thêm header Authorization.
                const response = await fetch(`/api/user/words/${wordId}`, { method: 'POST' });

                if (response.ok) {
                    //saveStatusDiv.innerHTML = '<p style="color: green;">Đã lưu từ thành công!</p>';
                    showNotification('Đã lưu từ thành công!'); // <-- Thay thế bằng dòng này
                    button.textContent = 'Đã lưu';
                    button.disabled = true;
                } else {
                    throw new Error('Không thể lưu từ.');
                }
            } catch (error) {
                console.error('Lỗi khi lưu từ:', error);
                saveStatusDiv.innerHTML = '<p class="error">Có lỗi xảy ra, không thể lưu từ.</p>';
            }
        }
    });
    function showNotification(message) {
        const notification = document.createElement('div');
        notification.className = 'notification';
        notification.textContent = message;
        document.body.appendChild(notification);

        // Hiển thị notification
        setTimeout(() => {
            notification.classList.add('show');
        }, 10);

        // Tự động ẩn sau 3 giây
        setTimeout(() => {
            notification.classList.remove('show');
            // Xóa khỏi DOM sau khi animation kết thúc
            setTimeout(() => {
                document.body.removeChild(notification);
            }, 500);
        }, 3000);
    }
});