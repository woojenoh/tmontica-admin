export interface TMessageError {
  message: string;
}

export interface TCommonError extends TMessageError {
  timestamp: string;
  status: number;
  error: string;
  path: string;
}
