
// Quiz Edit Modal open from button
window.openEditQuizModalFromButton = function (button) {
  const quizId = button.getAttribute("data-quiz-id");
  const lessonId = button.getAttribute("data-lesson-id");
  fetch(`/api/quizzes/${quizId}`)
    .then((res) => res.json())
    .then((quiz) => {
      document.getElementById("editQuizId").value = quiz.id;
      document.getElementById("editQuizTitle").value = quiz.title;
      document.getElementById("editQuizLessonId").value = lessonId;
      // Render questions
      const container = document.getElementById("editQuizQuestionsContainer");
      container.innerHTML = "";
      (quiz.questions || []).forEach((q, idx) => {
        container.appendChild(renderEditQuestion(q, idx));
      });
      new bootstrap.Modal(document.getElementById("editQuizModal")).show();
    });
};

function renderEditQuestion(q, idx) {
  const div = document.createElement("div");
  div.className = "card mb-3 edit-question-block";
  div.innerHTML = `
    <div class="card-body">
      <input type="hidden" class="edit-question-id" value="${q.id ?? ''}">
      <div class="mb-2"><label>Question</label>
        <input type="text" class="form-control edit-question-text" value="${q.text ?? ''}" required></div>
      <div class="row">
        <div class="col-md-6 mb-2"><label>A</label>
          <input type="text" class="form-control edit-option1" value="${q.option1 ?? ''}" required></div>
        <div class="col-md-6 mb-2"><label>B</label>
          <input type="text" class="form-control edit-option2" value="${q.option2 ?? ''}" required></div>
        <div class="col-md-6 mb-2"><label>C</label>
          <input type="text" class="form-control edit-option3" value="${q.option3 ?? ''}" required></div>
        <div class="col-md-6 mb-2"><label>D</label>
          <input type="text" class="form-control edit-option4" value="${q.option4 ?? ''}" required></div>
      </div>
      <div class="mb-2">
        <label>Correct Option</label>
        <select class="form-select edit-correct-option">
          <option value="0" ${q.correctOptionIndex === 0 ? 'selected' : ''}>A</option>
          <option value="1" ${q.correctOptionIndex === 1 ? 'selected' : ''}>B</option>
          <option value="2" ${q.correctOptionIndex === 2 ? 'selected' : ''}>C</option>
          <option value="3" ${q.correctOptionIndex === 3 ? 'selected' : ''}>D</option>
        </select>
      </div>
      <button type="button" class="btn btn-danger btn-sm delete-question-btn">Delete Question</button>
    </div>
  `;
  div.querySelector('.delete-question-btn').onclick = function() {
    div.setAttribute('data-delete', 'true');
    div.style.display = 'none';
  };
  return div;
}


// Add Question button
if (document.getElementById("addQuestionBtn")) {
  document.getElementById("addQuestionBtn").onclick = function() {
    const container = document.getElementById("editQuizQuestionsContainer");
    container.appendChild(renderEditQuestion({}, container.children.length));
  };
}

// Edit Quiz submit
if (document.getElementById("editQuizForm")) {
  document.getElementById("editQuizForm").onsubmit = function(e) {
    e.preventDefault();
    const quizId = document.getElementById("editQuizId").value;
    const title = document.getElementById("editQuizTitle").value;
    const questionBlocks = document.querySelectorAll("#editQuizQuestionsContainer .edit-question-block");
    const questions = [];
    questionBlocks.forEach(block => {
      const isDelete = block.getAttribute('data-delete') === 'true';
      const id = block.querySelector('.edit-question-id').value || null;
      const text = block.querySelector('.edit-question-text').value;
      const option1 = block.querySelector('.edit-option1').value;
      const option2 = block.querySelector('.edit-option2').value;
      const option3 = block.querySelector('.edit-option3').value;
      const option4 = block.querySelector('.edit-option4').value;
      const correctOptionIndex = parseInt(block.querySelector('.edit-correct-option').value);
      questions.push({ id, text, option1, option2, option3, option4, correctOptionIndex, delete: isDelete });
    });
    fetch(`/api/quizzes/${quizId}/edit-full`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ title, questions }),
    })
      .then(res => {
        if (!res.ok) throw new Error("Failed to update quiz");
        return res.json();
      })
      .then(() => {
        location.reload();
      })
      .catch(err => alert(err.message));
  };
}
// Quiz Delete
window.deleteQuiz = function (quizId) {
  if (!confirm("Are you sure you want to delete this quiz?")) return;
  fetch(`/api/quizzes/${quizId}`, { method: "DELETE" })
    .then((res) => {
      if (!res.ok && res.status !== 204)
        throw new Error("Failed to delete quiz");
      location.reload();
    })
    .catch((err) => alert(err.message));
};
// JS for lesson management on Course Details page
// Assumes courseId is available as a global variable or data attribute


// Add Lesson
if (document.getElementById("addLessonForm")) {
  document.getElementById("addLessonForm").addEventListener("submit", function (e) {
    e.preventDefault();
    const courseId = document.getElementById("courseDetailsContainer")?.getAttribute("data-course-id");
    const title = document.getElementById("lessonTitle").value;
    const content = document.getElementById("lessonContent").value;
    const lessonOrder = document.getElementById("lessonOrder").value;
    fetch("/lessons", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ title, content, lessonOrder, courseId }),
    })
      .then((res) => {
        if (!res.ok) throw new Error("Failed to add lesson");
        return res.json();
      })
      .then(() => {
        location.reload();
      })
      .catch((err) => alert(err.message));
  });
}

// Edit Lesson Modal open
window.openEditLessonModal = function (lessonId) {
  fetch(`/lessons/${lessonId}`)
    .then((res) => res.json())
    .then((lesson) => {
      document.getElementById("editLessonId").value = lesson.id;
      document.getElementById("editLessonTitle").value = lesson.title;
      document.getElementById("editLessonContent").value = lesson.content;
      document.getElementById("editLessonOrder").value = lesson.lessonOrder;
      new bootstrap.Modal(document.getElementById("editLessonModal")).show();
    });
};

// Edit Lesson submit
if (document.getElementById("editLessonForm")) {
  document.getElementById("editLessonForm").addEventListener("submit", function (e) {
    e.preventDefault();
    const courseId = document.getElementById("courseDetailsContainer")?.getAttribute("data-course-id");
    const id = document.getElementById("editLessonId").value;
    const title = document.getElementById("editLessonTitle").value;
    const content = document.getElementById("editLessonContent").value;
    const lessonOrder = document.getElementById("editLessonOrder").value;
    fetch(`/lessons/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ title, content, lessonOrder, courseId }),
    })
      .then((res) => {
        if (!res.ok) throw new Error("Failed to update lesson");
        return res.json();
      })
      .then(() => {
        location.reload();
      })
      .catch((err) => alert(err.message));
  });
}

// Delete Lesson
window.deleteLesson = function (lessonId) {
  const courseId = document.getElementById("courseDetailsContainer")?.getAttribute("data-course-id");
  if (!confirm("Are you sure you want to delete this lesson?")) return;
  fetch(`/lessons/${lessonId}?courseId=${courseId}`, { method: "DELETE" })
    .then((res) => {
      if (!res.ok && res.status !== 204)
        throw new Error("Failed to delete lesson");
      location.reload();
    })
    .catch((err) => alert(err.message));
};


