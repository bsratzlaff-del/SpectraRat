export interface RecommendationResult {
  id: number;
  status: string;
  matchPercentage: number;
  modelName: string;
  description: string;
}