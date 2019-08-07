export interface TMessageError {}

export interface TExceptionError {
  field?: string;
  exceptionMessage?: string;
  errors?: any;
}
export interface TCommonError extends TMessageError, TExceptionError {
  timestamp?: string;
  status?: number;
  error?: string;
  path?: string;
  message?: string;
}
