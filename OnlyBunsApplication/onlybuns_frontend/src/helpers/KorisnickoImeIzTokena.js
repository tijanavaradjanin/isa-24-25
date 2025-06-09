export const getToken = () => localStorage.getItem("token");

export const parseJwt = () => {
  const token = getToken();
  if (!token) return null;

  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload;
  } catch (error) {
    console.error("Neispravan JWT token", error);
    return null;
  }
};

export const korisnickoImeIzTokena = () => {
  const payload = parseJwt();
  return payload?.sub || null;
};
