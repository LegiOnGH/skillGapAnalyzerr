export const unwrapPage = (data) => ({
  content: data?.content ?? [],
  totalPages: data?.totalPages ?? 0,
  totalElements: data?.totalElements ?? 0,
  currentPage: data?.number ?? 0,
  pageSize: data?.size ?? 0,
});