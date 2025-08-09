document.addEventListener('DOMContentLoaded', () => {
    const table = document.querySelector('.word-table');
    if (!table) return;

    table.addEventListener('click', async (event) => {
        if (event.target.classList.contains('delete-btn')) {
            const button = event.target;
            const wordId = button.dataset.wordId;

            // Hỏi xác nhận trước khi xóa
            if (confirm('Bạn có chắc chắn muốn xóa từ này khỏi danh sách?')) {
                try {
                    const response = await fetch(`/api/user/words/${wordId}`, {
                        method: 'DELETE'
                    });

                    if (response.ok) {
                        // Xóa hàng tương ứng khỏi bảng trên giao diện
                        const row = document.getElementById(`word-row-${wordId}`);
                        row.remove();
                    } else {
                        alert('Xóa thất bại. Vui lòng thử lại.');
                    }
                } catch (error) {
                    console.error('Lỗi khi xóa từ:', error);
                    alert('Đã xảy ra lỗi kết nối.');
                }
            }
        }
    });
});