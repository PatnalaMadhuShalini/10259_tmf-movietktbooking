<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/layout/header.jsp" />

<jsp:include page="/WEB-INF/views/layout/navbar.jsp" />

<div class="container container-main">
    <!-- CONTENT BLOCK -->
    <jsp:include page="${contentPage}" />
</div>

<jsp:include page="/WEB-INF/views/layout/footer.jsp" />