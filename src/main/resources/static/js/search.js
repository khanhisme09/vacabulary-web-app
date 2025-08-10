document.addEventListener('DOMContentLoaded', () => {
    const searchForm = document.getElementById('search-form');
    const searchInput = document.getElementById('search-input');
    const resultsDiv = document.getElementById('search-results');

    searchForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        const searchTerm = searchInput.value.trim();
        if (!searchTerm) return;

        resultsDiv.innerHTML = `
            <div class="mt-8 text-center">
                <p class="text-gray-500">Đang tìm kiếm...</p>
            </div>
        `;

        try {
            const response = await fetch(`/api/words/search?q=${encodeURIComponent(searchTerm)}`);
            if (!response.ok) {
                if (response.status === 404) {
                    resultsDiv.innerHTML = `<div class="mt-8 p-6 bg-white rounded-lg shadow text-center"><p class="text-red-600">Không tìm thấy từ: <strong>${searchTerm}</strong></p></div>`;
                } else { throw new Error('Lỗi từ server!'); }
                return;
            }
            const data = await response.json();
            displayWord(data);
        } catch (error) {
            console.error('Có lỗi xảy ra:', error);
            resultsDiv.innerHTML = `<div class="mt-8 p-6 bg-white rounded-lg shadow text-center"><p class="text-red-600">Không thể thực hiện tìm kiếm. Vui lòng thử lại.</p></div>`;
        }
    });

    function displayWord(wordData) {
        const isAuthenticated = document.querySelector('div[sec\\:authorize="isAuthenticated"]') != null;
        let saveButtonHtml = '';
        if (isAuthenticated) {
            saveButtonHtml = `<button class="save-word-btn absolute top-6 right-6 px-4 py-2 text-sm font-medium text-white bg-indigo-600 rounded-lg hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-colors" data-word-id="${wordData.id}">Lưu từ</button>`;
        }

        resultsDiv.innerHTML = `
            <div class="mt-8 p-6 bg-white rounded-lg shadow-lg relative animate-fade-in">
                ${saveButtonHtml}
                <div class="flex items-baseline space-x-4">
                    <h2 class="text-3xl font-bold text-gray-900">${wordData.word}</h2>
                    <span class="text-xl text-gray-500">${wordData.phonetic || ''}</span>
                </div>
                <div class="mt-4 border-t pt-4">
                    <h3 class="font-semibold text-gray-800">Định nghĩa:</h3>
                    <p class="text-gray-700 leading-relaxed">${wordData.definition}</p>
                </div>
                <div class="mt-4 p-4 bg-gray-50 rounded-md border-l-4 border-indigo-500">
                    <h3 class="font-semibold text-gray-800">Ví dụ:</h3>
                    <em class="text-gray-700">"${wordData.example || 'Không có ví dụ.'}"</em>
                </div>
                <div id="save-status" class="mt-4 text-sm font-medium"></div>
            </div>
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
                     new Error('Không thể lưu từ.');
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