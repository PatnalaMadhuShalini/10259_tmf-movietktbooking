<div class="container mt-4">
    <div class="alert alert-warning">
        <h4>Not Found</h4>
        <p>${message != null ? message : 'The item you requested could not be found.'}</p>
        <a href="${pageContext.request.contextPath}/movies" class="btn btn-primary">Back to Movies</a>
    </div>
</div>
