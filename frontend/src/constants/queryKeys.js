export const QUERY_KEYS = {
  CATEGORIES: ["categories"],
  ROLES: (category) => ["roles", category],
  ROLE_BY_NAME: (name) => ["role", name],
  ALL_SKILLS: ["allSkills"],
  ANALYSIS_HISTORY: (page, size) => ["analysis", page, size],
  ANALYSIS_BY_ID: (id) => ["analysis", id],
};