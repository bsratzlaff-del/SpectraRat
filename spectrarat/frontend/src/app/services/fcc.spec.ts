import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { FccService } from './fcc.service';
import { FrequencyBand } from '../models/frequency-band';
import { environment } from '../../environments/environment';

describe('FccService', () => {
  let service: FccService;
  let httpTestingController: HttpTestingController;
  let apiUrl: string;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [FccService]
    });
    service = TestBed.inject(FccService);
    httpTestingController = TestBed.inject(HttpTestingController);
    // From fcc.service.ts, the base URL is constructed from the environment
    apiUrl = `${environment.apiUrl}/frequency-bands`;
  });

  afterEach(() => {
    // After every test, assert that there are no more pending requests.
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getFrequencyBands', () => {
    it('should return an Observable<FrequencyBand[]>', () => {
      const mockBands: FrequencyBand[] = [
        { id: 1, bandName: 'G50', minFrequency: 470.0, maxFrequency: 534.0 },
        { id: 2, bandName: 'H50', minFrequency: 518.0, maxFrequency: 572.0 }
      ];

      service.getFrequencyBands().subscribe(bands => {
        expect(bands).toEqual(mockBands);
      });

      const req = httpTestingController.expectOne(apiUrl);
      expect(req.request.method).toEqual('GET');
      req.flush(mockBands);
    });
  });

  describe('validateApiConnection', () => {
    it('should return a validation string', () => {
        const mockResponse = "API Connection OK";
        const testUrl = `${apiUrl}/validate`;

        service.validateApiConnection().subscribe(response => {
            expect(response).toEqual(mockResponse);
        });

        const req = httpTestingController.expectOne(testUrl);
        expect(req.request.method).toEqual('GET');
        expect(req.request.responseType).toEqual('text');
        req.flush(mockResponse);
    });
  });
});